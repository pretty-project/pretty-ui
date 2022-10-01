
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.helpers
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [re-frame.api       :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (item-editor/env->item-suggestions {:params {:suggestion-keys [:my-key :your-key]}} :my-editor)
  ;  =>
  ;  {:my-type/my-key   ["..."]
  ;   :my-type/your-key ["..." "..."]}
  ;
  ; @return (map)
  [env editor-id]
  (let [item-namespace  @(r/subscribe [:item-editor/get-editor-prop editor-id :item-namespace])
        collection-name @(r/subscribe [:item-editor/get-editor-prop editor-id :collection-name])
        suggestion-keys  (pathom/env->param env :suggestion-keys)
        collection       (mongo-db/get-collection collection-name)]
       (letfn [; Az f0 függvény kiolvassa a dokumentumból a suggestion-key kulcshoz tartozó értékét.
               ; Ha a suggestion-key kulcshoz tartozó érték ...
               ; ... üres (nil, "", [], ...), akkor változtatás nélkül visszatér a suggestions térképpel.
               ; ... string típus, akkor hozzáadja a suggestions térképhez.
               ; ... vektor típus, akkor az elemeit hozzáadja a suggestions térképhez.
               ; ... egyéb esetben változtatás nélkül visszatér a suggestions térképpel.
               (f0 [suggestions document suggestion-key-dex]
                   (let [suggestion-key (nth suggestion-keys suggestion-key-dex)
                         k              (keyword (name item-namespace) (name suggestion-key))
                         v              (get document k)]
                        (cond (empty?  v) (return suggestions)
                              (string? v) (update suggestions suggestion-key vector/conj-item-once    v)
                              (vector? v) (update suggestions suggestion-key vector/concat-items-once v)
                              :else       (return suggestions))))
               ; Az f1 függvény végigiterál a suggestion-keys vektor elemein.
               (f1 [suggestions document suggestion-key-dex]
                   (let [suggestions (f0 suggestions document suggestion-key-dex)]
                        (if (vector/dex-last? suggestion-keys suggestion-key-dex)
                            (return suggestions)
                            (f1     suggestions document (inc suggestion-key-dex)))))
               ; Az f2 függvény végigiterál a kollekció elemein.
               (f2 [suggestions document-dex]
                   (let [suggestions (f1 suggestions (nth collection document-dex) 0)]
                        (if (vector/dex-last? collection document-dex)
                            (return suggestions)
                            (f2     suggestions (inc document-dex)))))
               ; Az f3 függvény végigiterál a suggestions térkép értékein és kitörli belőlük
               ; az üres értékeket (nil, "").
               (f3 [suggestions] (map/->values suggestions #(vector/remove-items % [nil ""])))]
              ; Ha a collection vagy a suggestion-keys vektor nem tartalmaz elemeket, akkor
              ; a függvény visszatérési értéke egy üres térkép.
              (if (or (empty? collection)
                      (empty? suggestion-keys))
                  (return {})
                  (f3 (f2 {} 0))))))
