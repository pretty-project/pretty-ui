
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.events
    (:require [candy.api                          :refer [return]]
              [engines.engine-handler.core.events :as core.events]
              [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.core.subs     :as core.subs]
              [engines.item-browser.items.events  :as items.events]
              [map.api                            :refer [dissoc-in]]
              [engines.item-lister.core.events    :as engines.item-lister.core.events]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-query-param! core.events/set-query-param!)
(def set-item-id!     core.events/set-item-id!)
(def clear-item-id!   core.events/clear-item-id!)
(def derive-item-id!  core.events/derive-item-id!)

; engines.item-lister.core.events
(def set-meta-item!        engines.item-lister.core.events/set-meta-item!)
(def remove-meta-item!     engines.item-lister.core.events/remove-meta-item!)
(def remove-meta-items!    engines.item-lister.core.events/remove-meta-items!)
(def set-engine-error!     engines.item-lister.core.events/set-engine-error!)
(def reset-downloads!      engines.item-lister.core.events/reset-downloads!)
(def use-default-order-by! engines.item-lister.core.events/use-default-order-by!)
(def filter-items!         engines.item-lister.core.events/filter-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#0168
  ; Az aktuálisan böngészett elem azonosítójának eltárolásakor az engine query-params
  ; térképében is szükséges eltárolni az elem azonosítóját, mert az item-browser engine az
  ; item-lister engine letöltő funkciójával tölti le az elemeket, de az item-lister engine
  ; nem használ :item-id paramétert az elemek letöltésekor ezért az item-browser engine
  ; a query-params használatával teszi bele az aktuálisan böngészett elem azonosíját
  ; a Pathom lekéresekkel elküldött adatok közé.
  (let [current-item-id (r core.subs/get-current-item-id db browser-id)]
       (r set-query-param! db browser-id :item-id current-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  (as-> db % (r derive-item-id! % browser-id)
             (r use-item-id!    % browser-id)))

(defn reload-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#1400
  ; When the body component's :item-id parameter has been changed ...
  ; ... the clear-item-id! function has to be applied, otherwise the
  ;     update-item-id! function would ignores the updating if it sees
  ;     an id already set in the engine.
  ; ... the update-item-id! function derives the current id from the
  ;     body component's parameters.
  ; ... the use-item-id! function has to be applied according to: XXX#0168.
  (as-> db % (r remove-meta-item! % browser-id :engine-error)
             (r derive-item-id!   % browser-id)
             (r use-item-id!      % browser-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-browser/set-engine-error! set-engine-error!)
