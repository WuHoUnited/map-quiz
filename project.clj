(defproject map-quiz "0.1.0-SNAPSHOT"
  :description "In-Browser Geography Quiz."
  :url "https://github.com/WuHoUnited/map-quiz"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2197"
                  :exclusions [org.apache.ant/ant]]
                 [prismatic/dommy "0.1.2"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild {
              :builds [{:id "prod"
                        :source-paths ["src-cljs"]
                        :compiler {
                                   :output-to "public/prod/map_quiz.js"
                                   :optimizations :advanced}}
                       {:id "fifteen"
                        :source-paths ["src-cljs"]
                        :compiler {
                                   :output-to "public/dev/map_quiz.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}]})
