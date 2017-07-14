(defproject org.clojars.mike706574/eval "0.0.1-SNAPSHOT"
  :description "A project."
  :url "https://github.com/mike706574/eval-example"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/spec.alpha "0.1.123"]
                 [org.clojure/clojurescript "1.9.671"]
                 [com.taoensso/timbre "4.10.0"]
                 [reagent "0.7.0"]
                 [re-frame "0.9.4"]
                 [binaryage/devtools "0.9.4"]]
  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-doo "0.1.7"]
            [lein-figwheel "0.5.11"]]
  :source-paths ["src/cljs"]
  :hooks [leiningen.cljsbuild]
  :profiles {:test {:source-paths ["src" "test"]
                    :compiler {:output-to "target/test.js"
                               :output-dir "target/test"
                               :main "eval.runner"
                               :optimizations :simple}}
             :dev {:source-paths ["dev"]
                   :target-path "target/dev"
                   :dependencies [[com.cemerick/piggieback "0.2.2"]
                                  [figwheel-sidecar "0.5.11"]
                                  [org.clojure/tools.namespace "0.2.11"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]
                             :figwheel {:on-jsload "eval.core/run"}
                             :compiler {:output-to "resources/public/js/client.js"
                                        :output-dir "resources/public/js"
                                        :main "eval.core"
                                        :asset-path "js"
                                        :optimizations :none
                                        :source-map true
                                        :source-map-timestamp true}}}}
  :doo {:build "test"
        :repl false
        :alias {:default [:phantom]
                :browsers [:chrome :firefox]
                :all [:browsers :headless]}}
  :figwheel {:repl false
             :http-server-root "public"}
  :clean-targets ^{:protect false} ["resources/public/js"])
