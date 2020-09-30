(defproject konva-cljs "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [reagent "0.10.0"]
                 [thheller/shadow-cljs "2.11.4"]]

  :plugins [[lein-shadow "0.3.1"]
            [lein-shell "0.5.0"]]

  :min-lein-version "2.9.0"

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :shadow-cljs {:nrepl  {:port 8777}

                :builds {:app {:target     :browser
                               :output-dir "resources/public/js/compiled"
                               :asset-path "/js/compiled"
                               :modules    {:app {:init-fn  konva-cljs.views/init
                                                  :preloads [devtools.preload]}}
                               :devtools   {:http-root "resources/public"
                                            :http-port 8280}}
                         :browser-test
                              {:target    :browser-test
                               :ns-regexp "-test$"
                               :runner-ns shadow.test.browser
                               :test-dir  "target/browser-test"
                               :devtools  {:http-root "target/browser-test"
                                           :http-port 8290}}

                         :karma-test
                              {:target    :karma
                               :ns-regexp "-test$"
                               :output-to "target/karma-test.js"}}}

  :shell {:commands {"karma" {:windows         ["cmd" "/c" "karma"]
                              :default-command "karma"}
                     "open"  {:windows ["cmd" "/c" "start"]
                              :macosx  "open"
                              :linux   "xdg-open"}}}

  :aliases {"dev"          ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein watch instead.\""]
                            ["watch"]]
            "watch"        ["with-profile" "dev" "do"
                            ["shadow" "watch" "app" "browser-test" "karma-test"]]

            "prod"         ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein release instead.\""]
                            ["release"]]

            "release"      ["with-profile" "prod" "do"
                            ["shadow" "release" "app"]]

            "build-report" ["with-profile" "prod" "do"
                            ["shadow" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]

            "karma"        ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein ci instead.\""]
                            ["ci"]]
            "ci"           ["with-profile" "prod" "do"
                            ["shadow" "compile" "karma-test"]
                            ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]}

  :profiles
  {:dev  {:dependencies [[binaryage/devtools "1.0.2"]]
          :source-paths ["dev"]}

   :prod {}

   }

  :prep-tasks [])
