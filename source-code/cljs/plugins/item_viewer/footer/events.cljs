
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.footer.events
    (:require [plugins.plugin-handler.footer.events :as footer.events]
              [x.app-core.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.footer.events
(def store-footer-props!  footer.events/store-footer-props!)
(def remove-footer-props! footer.events/remove-footer-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ viewer-id footer-props]]
  (r store-footer-props! db viewer-id footer-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (r remove-footer-props! db viewer-id))
