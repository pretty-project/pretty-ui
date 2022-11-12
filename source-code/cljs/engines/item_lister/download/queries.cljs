
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.queries
    (:require [candy.api                         :refer [return]]
              [engines.item-lister.body.subs     :as body.subs]
              [engines.item-lister.core.subs     :as core.subs]
              [engines.item-lister.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-items-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter-pattern (maps in vector)
  ;   :order-by (namespaced keyword)
  ;   :reload-items? (boolean)
  ;   :search-keys (keywords in vector)
  ;   :search-term (string)}
  [db [_ lister-id]]
  (r core.subs/use-query-params db lister-id {:download-limit        (r body.subs/get-body-prop             db lister-id :download-limit)
                                              :order-by              (r core.subs/get-meta-item             db lister-id :order-by)
                                              :reload-items?         (r core.subs/get-meta-item             db lister-id :reload-mode?)
                                              :search-keys           (r core.subs/get-meta-item             db lister-id :search-keys)
                                              :search-term           (r core.subs/get-meta-item             db lister-id :search-term)
                                              :downloaded-item-count (r core.subs/get-downloaded-item-count db lister-id)
                                              :filter-pattern        (r core.subs/get-filter-pattern        db lister-id)}))

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (vector)
  [db [_ lister-id]]
  (let [resolver-id    (r download.subs/get-resolver-id    db lister-id :get-items)
        resolver-props (r get-request-items-resolver-props db lister-id)
        query          [`(~resolver-id ~resolver-props)]]
       ; XXX#9981
       ; Az item-lister engine body komponense számára {:query [...]} tulajdonságként
       ; átadott Pathom lekérés vektort csak abban az esetben fűzi össze az elemek
       ; letöltéséhez készített lekérés vektorral, ha még nem töltődött le egyetlen
       ; elem sem, tehát a {:query [...]} tulajdonság csak az elemek első letöltésekor
       ; van elküldve!
       (if (r download.subs/first-data-received? db lister-id)
           (return query)
           (r core.subs/use-query-prop db lister-id query))))
