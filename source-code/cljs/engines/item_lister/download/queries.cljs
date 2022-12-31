
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.queries
    (:require [candy.api                         :refer [return]]
              [engines.item-lister.body.subs     :as body.subs]
              [engines.item-lister.core.subs     :as core.subs]
              [engines.item-lister.download.subs :as download.subs]
              [map.api                           :as map]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-items-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  ; {:download-limit (integer)
  ;  :filter-pattern (maps in vector)
  ;  :listed-item-count (integer)
  ;  :order-by (namespaced keyword)
  ;  :reload-items? (boolean)
  ;  :search-keys (keywords in vector)
  ;  :search-term (string)}
  [db [_ lister-id]]
  (as-> {} % (map/assoc-some % :download-limit    (r body.subs/get-body-prop         db lister-id :download-limit))
             (map/assoc-some % :order-by          (r core.subs/get-meta-item         db lister-id :order-by))
             (map/assoc-some % :reload-items?     (r core.subs/get-meta-item         db lister-id :reload-mode?))
             (map/assoc-some % :search-keys       (r core.subs/get-meta-item         db lister-id :search-keys))
             (map/assoc-some % :search-term       (r core.subs/get-meta-item         db lister-id :search-term))
             (map/assoc-some % :listed-item-count (r core.subs/get-listed-item-count db lister-id))
             (map/assoc-some % :filter-pattern    (r core.subs/get-filter-pattern    db lister-id))
             (r core.subs/use-query-params db lister-id %)))

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
       ; kerül elküldésre!
       (if (r download.subs/first-data-received? db lister-id)
           (return query)
           (r core.subs/use-query-prop db lister-id query))))
