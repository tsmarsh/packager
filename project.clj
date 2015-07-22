(defproject tsmarsh/packager "0.1.0"
  :description "Clojure library to pack 2D shapes into a 2D container"
  :url "http://tailoredshapes.com"
  :license {:name "BSD 2 Clause"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [prismatic/schema "0.4.3"]]
  :profiles {:dev
             {:dependencies [[org.clojure/test.check "0.7.0"]]}}
  :plugins [[lein-cloverage "1.0.6"]
            [lein-cljfmt "0.1.12"]])
