
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.helpers
    (:require [candy.api    :refer [return]]
              [map.api      :as map]
              [mixed.api    :as mixed]
              [mongo-db.api :as mongo-db]
              [pathom.api   :as pathom]
              [re-frame.api :as r]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
  ; @param (keyword) handler-id
  ;
  ; @example
  ; (env->item-suggestions {:params {:suggestion-keys [:my-key :your-key]}} :my-handler)
  ; =>
  ; {:my-type/my-key   ["..."]
  ;  :my-type/your-key ["..." "..."]}
  ;
  ; @return (map)
  [env handler-id]
  ; Az f0 függvény kiolvassa a dokumentumból a suggestion-key kulcshoz tartozó értékét.
  ; Ha a suggestion-key kulcshoz tartozó érték ...
  ; ... üres (nil, "", [], ...), akkor változtatás nélkül visszatér a suggestions térképpel.
  ; ... string típus, akkor hozzáadja a suggestions térképhez.
  ; ... vektor típus, akkor az elemeit hozzáadja a suggestions térképhez.
  ; ... egyéb esetben változtatás nélkül visszatér a suggestions térképpel.
  ;
  ; Az f1 függvény végigiterál a suggestion-keys vektor elemein.
  ;
  ; Az f2 függvény végigiterál a kollekció elemein.
  ;
  ; Az f3 függvény végigiterál a suggestions térkép értékein és kitörli belőlük
  ; az üres értékeket (nil, "").
  (let [item-namespace  @(r/subscribe [:item-handler/get-handler-prop handler-id :item-namespace])
        collection-name @(r/subscribe [:item-handler/get-handler-prop handler-id :collection-name])
        suggestion-keys  (pathom/env->param env :suggestion-keys)
        collection       (mongo-db/get-collection collection-name)]
       (letfn [(f0 [suggestions document suggestion-key-dex]
                   (let [suggestion-key (nth suggestion-keys suggestion-key-dex)
                         k              (keyword (name item-namespace) (name suggestion-key))
                         v              (get document k)]
                        ; A mixed/blank? függvényt a string? és vector? feltétel előtt szükséges alkalmazni!
                        (cond ; Non-seqable values ...
                              (keyword?     v) (update suggestions suggestion-key vector/conj-item-once    v)
                              (number?      v) (update suggestions suggestion-key vector/conj-item-once    v)
                              ; Seqable values ...
                              (mixed/blank? v) (return suggestions)
                              (string?      v) (update suggestions suggestion-key vector/conj-item-once    v)
                              (vector?      v) (update suggestions suggestion-key vector/concat-items-once v)
                              :return suggestions)))

               (f1 [suggestions document suggestion-key-dex]
                   (let [suggestions (f0 suggestions document suggestion-key-dex)]
                        (if (vector/dex-last? suggestion-keys suggestion-key-dex)
                            (return suggestions)
                            (f1     suggestions document (inc suggestion-key-dex)))))

               (f2 [suggestions document-dex]
                   (let [suggestions (f1 suggestions (nth collection document-dex) 0)]
                        (if (vector/dex-last? collection document-dex)
                            (return suggestions)
                            (f2     suggestions (inc document-dex)))))

               (f3 [suggestions] (map/->values suggestions #(vector/remove-items % [nil ""])))]

              ; Ha a collection vagy a suggestion-keys vektor nem tartalmaz elemeket, akkor
              ; a függvény visszatérési értéke egy üres térkép.
              (if (or (empty? collection)
                      (empty? suggestion-keys))
                  (return {})
                  (f3 (f2 {} 0))))))
