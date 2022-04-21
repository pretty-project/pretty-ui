
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.footer.events
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
  ; @param (keyword) editor-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ editor-id footer-props]]
  (r store-footer-props! db editor-id footer-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r remove-footer-props! db editor-id))
