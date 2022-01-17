


; Mondjuk feltöltesz 3 elemet ...
; - Nem szükséges a letöltött elemek azonosítóit elküldeni a szervernek (ez tök felesleges)!
; - Ha pl. a limit értéke 20, és eddig 2 adagot kértél le, akkor változáskor
;   kérd le 0-40-ig az elemeket, mert te akkor a 0-40 range-et látod és tök mindegy, hogy ebben
;   a range-ben eddig csak mondjuk 21 volt, akkor is a 0-40 ranget látod, szoval azt kérjed!
; - Hogy ha van letöltve 21/21 és a legaljára vagy szkrollova, akkor
;   elindul a request (0-40) és lejön 24/24 elem esetleg ha sok az új elem,
;   akkor az infinite-loader addig tölt, amig ki nem ér a viewport-bol.
; - Nem baj, ha esetleg nem jön le egy olyan elem ami eddig lennt volt!
;   Szóval nem cink ha az uj elemek kitolják a 0-40 range-böl amik eddig lennt voltak,
;   mert csak akkor tehetik meg, ha nem vagy az alján és nem látod az infinite-loader-t!
;   Ha látnád az infinite-loader-t, akkor nem tudná kitolni, mert az már kapásbol szedné is le
;   a többi elemet, ami kifér a képernyőre!
;   mert az infinite-loader miatt csak olyan, mint ha kiszuszna abbol a range-böl,
;   amit belátsz, de ha leszkrollosz, mert keresed akkor jön le ujbol az infinite-loader-rel!
; Végiggondoltam, próbáld ki kajak zura igy!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.7.6
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.queries
    (:require [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-items-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter-pattern (map)
  ;   :order-by (keyword)
  ;   :reload-items? (boolean)
  ;   :search-keys (keywords in vector)
  ;   :search-term (string)}
  [db [_ extension-id item-namespace]]
  {:downloaded-item-count (r subs/get-downloaded-item-count db extension-id)
   :download-limit        (r subs/get-meta-value            db extension-id item-namespace :download-limit)
   :filter-pattern        (r subs/get-meta-value            db extension-id item-namespace :filter-pattern)
   :order-by              (r subs/get-meta-value            db extension-id item-namespace :order-by)
   :reload-items?         (r subs/get-meta-value            db extension-id item-namespace :reload-mode?)
   :search-keys           (r subs/get-meta-value            db extension-id item-namespace :search-keys)
   :search-term           (r subs/get-search-term           db extension-id item-namespace)

   ; TEMP
   ; Az {:item-id ...} értéke az item-browser plugin számára szükséges!
   :item-id (get-in db [extension-id :item-browser/meta-items :item-id])})

(defn get-delete-selected-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name (engine/mutation-name            extension-id item-namespace :delete)
        item-ids      (r subs/get-selected-item-ids db extension-id item-namespace)]
       [:debug `(~(symbol mutation-name) ~{:item-ids item-ids})]))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (engine/mutation-name          extension-id item-namespace :undo-delete)
        exported-items (r subs/export-backup-items db extension-id item-namespace item-ids)]
       [:debug `(~(symbol mutation-name) ~{:items exported-items})]))

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id    (engine/resolver-id                    extension-id item-namespace :get)
        resolver-props (r get-request-items-resolver-props db extension-id item-namespace)]
       [:debug `(~resolver-id ~resolver-props)]))
