
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.events
    (:require [engines.engine-handler.body.events :as body.events]
              [engines.item-handler.backup.events :as backup.events]
              [engines.item-handler.core.events   :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.body.events
(def store-body-props!  body.events/store-body-props!)
(def remove-body-props! body.events/remove-body-props!)
(def update-body-props! body.events/update-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ handler-id body-props]]
  (r store-body-props! db handler-id body-props))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (as-> db % (r core.events/remove-meta-items!           % handler-id)
             (r core.events/reset-downloads!             % handler-id)
             (r backup.events/clean-current-item-backup! % handler-id)
             (r remove-body-props!                       % handler-id)))

(defn body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ handler-id body-props]]
  (r update-body-props! db handler-id body-props))
