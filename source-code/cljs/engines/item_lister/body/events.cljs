
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.body.events
    (:require [engines.engine-handler.body.events :as body.events]
              [engines.item-lister.core.events    :as core.events]
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
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ lister-id body-props]]
  (as-> db % (r store-body-props!                 % lister-id body-props)
             (r core.events/use-default-order-by! % lister-id)))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (r core.events/reset-meta-items! % lister-id)
             (r core.events/reset-downloads!  % lister-id)
             (r remove-body-props!            % lister-id)))

(defn body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ lister-id body-props]]
  (as-> db % (r core.events/reset-downloads!  % lister-id)
             (r core.events/reset-selections! % lister-id)
             (r update-body-props!            % lister-id body-props)))
