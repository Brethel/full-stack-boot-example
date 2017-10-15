(set-env!
  :source-paths #{"src/clj" "src/cljc" "src/cljs"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  ; project deps
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.946" :scope "test"]
                  [reagent "0.7.0" :scope "test"]
                  [ring "1.5.1"]])

(task-options!
  pom {:project 'full-stack-boot-example
       :version "1.0.0-SNAPSHOT"
       :description "FIXME: write description"}
  aot {:namespace '#{full-stack-boot-example.core}}
  jar {:main 'full-stack-boot-example.core}
  sift {:include #{#"\.jar$"}})

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  'full-stack-boot-example.core)

(deftask run []
  (comp
    (watch)
    (reload :asset-path "public")
    (cljs :source-map true :optimizations :none :compiler-options {:asset-path "main.out"})
    (target)
    (with-pass-thru _
      (full-stack-boot-example.core/dev-main))))

(deftask build []
  (comp
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)
    (sift)
    (target)))

