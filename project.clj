(defproject packager "0.1.0"
  :description "FIXME: write description"
  :url "http://tailoredshapes.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [prismatic/schema "0.4.3"]]
  :profiles {:dev
             {:dependencies [[org.clojure/test.check "0.7.0"]]}}
  :plugins [[lein-cloverage "1.0.6"]
            [lein-cljfmt "0.1.12"]])
