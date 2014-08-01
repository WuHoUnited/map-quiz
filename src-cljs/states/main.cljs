(ns states.main
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy])
  (:use-macros
   [dommy.macros :only [node sel sel1]]))

(def states ["AK" "AL" "AR" "AZ" "CA" "CO" "CT" "DC" "DE" "FL" "GA" "HI" "IA" "ID" "IL" "IN" "KS" "KY" "LA" "MA" "MD" "ME" "MI" "MN" "MO" "MS" "MT" "NC" "ND" "NE" "NH" "NJ" "NM" "NV" "NY" "OH" "OK" "OR" "PA" "RI" "SC" "SD" "TN" "TX" "UT" "VA" "VT" "WA" "WI" "WV" "WY"])

(defn create-new-app-state! []
  {:unguessed-states (shuffle states)
   :guessed-states: []})

(def app-state (atom nil))

(defn render-current-state! []
  (dommy/set-text! (sel1 "#current-state")
                   (-> @app-state
                       :unguessed-states
                       first)))

(defn mark-correct! [state]
  (dommy/add-class! (sel1 (str "#" state))
                    "correct"))

(defn handle-correct-guess! []
  (let [state (-> @app-state :unguessed-states first)]
    (mark-correct! state)
    (swap! app-state
           (fn [app-state]
             (-> app-state
                 (update-in [:unguessed-states] subvec 1)
                 (update-in [:guessed-states] conj state))))))

(defn mark-incorrect! [state]
  (dommy/add-class! (sel1 (str "#" state))
                    "incorrect"))

(defn handle-incorrect-guess! []
  (let [state (-> @app-state :unguessed-states first)]
    (mark-incorrect! state)
    (swap! app-state
           (fn [app-state]
             (-> app-state
                 (update-in [:unguessed-states] #(conj (subvec % 1) state)))))))

(defn guess! [state]
  (if (= state (-> @app-state :unguessed-states first))
    (handle-correct-guess!)
    (handle-incorrect-guess!))
  (render-current-state!))

(defn set-click-listeners! []
  (doseq [state states]
           (dommy/listen! (sel1 (str "#" state))
                          :click (fn [e]
                                   (guess! state)))))

(defn initialize []
  (reset! app-state (create-new-app-state!))
  (render-current-state!)
  (set-click-listeners!))

(initialize)
