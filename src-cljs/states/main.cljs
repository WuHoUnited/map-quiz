(ns states.main
  "Main entry point for web page"
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy])
  (:use-macros
   [dommy.macros :only [node sel sel1]]))

;; Define globals related to application state.

(def states
  "Holds the states. These are the \"id\"s of the states elements in the SVG.."
  ["AK" "AL" "AR" "AZ" "CA" "CO" "CT" "DC" "DE" "FL" "GA" "HI" "IA" "ID" "IL" "IN" "KS"
   "KY" "LA" "MA" "MD" "ME" "MI" "MN" "MO" "MS" "MT" "NC" "ND" "NE" "NH" "NJ" "NM" "NV"
   "NY" "OH" "OK" "OR" "PA" "RI" "SC" "SD" "TN" "TX" "UT" "VA" "VT" "WA" "WI" "WV" "WY"])

(def app-state
  "Holds the state of the application.
  Contains the guessed and unguessed states.
  The current state will be the first state in the :unguessed-states sequence."
  (atom nil))

(defn create-new-app-state!
  "Create a blank application state where no states have been guessed."
  []
  {:unguessed-states (shuffle states)
   :guessed-states: []})

;; Define DOM manipulation functions

(defn render-current-state!
  "Sets the text indicating what the current state being guessed is."
  []
  (dommy/set-text! (sel1 "#current-state")
                   (-> @app-state
                       :unguessed-states
                       first)))

(defn mark-correct!
  "Marks in the SVG that the given state is correct."
  [state]
  (dommy/add-class! (sel1 (str "#" state))
                    "correct"))

(defn mark-incorrect!
  "Update the SVG to indicate that the state has been guessed incorrectly."
  [state]
  (dommy/add-class! (sel1 (str "#" state))
                    "incorrect"))

(defn handle-correct-guess!
  "Updates the application state and DOM to indicate that the state has been correctly
  guessed."
  []
  (let [state (-> @app-state :unguessed-states first)]
    (mark-correct! state)
    (swap! app-state
           (fn [app-state]
             (-> app-state
                 (update-in [:unguessed-states] subvec 1)
                 (update-in [:guessed-states] conj state))))))

(defn handle-incorrect-guess!
  "Updates the application state and DOM to indicate that the state
  has been incorrectly guessed."
  []
  (let [state (-> @app-state :unguessed-states first)]
    (mark-incorrect! state)
    (swap! app-state
           (fn [app-state]
             (-> app-state
                 (update-in [:unguessed-states] #(conj (subvec % 1) state)))))))

(defn guess!
  "Process the user guess."
  [state]
  (if (= state (-> @app-state :unguessed-states first))
    (handle-correct-guess!)
    (handle-incorrect-guess!))
  (render-current-state!))

(defn set-click-listeners!
  "Add click listeners to the SVG."
  []
  (doseq [state states]
           (dommy/listen! (sel1 (str "#" state))
                          :click (fn [e]
                                   (guess! state)))))

;; Initialize the Application

(defn initialize
  "Initialize the application state, dom, listeners, etc."
  []
  (reset! app-state (create-new-app-state!))
  (render-current-state!)
  (set-click-listeners!))

(initialize)
