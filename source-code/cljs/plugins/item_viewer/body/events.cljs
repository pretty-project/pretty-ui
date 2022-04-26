
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.events
    (:require [plugins.item-viewer.core.events    :as core.events]
              [plugins.plugin-handler.body.events :as body.events]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.events
(def store-body-props!  body.events/store-body-props!)
(def remove-body-props! body.events/remove-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ viewer-id body-props]]
  (r store-body-props! db viewer-id body-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (as-> db % (r core.events/remove-meta-items! % viewer-id)
             (r core.events/reset-downloads!   % viewer-id)
             (r remove-body-props!             % viewer-id)))
