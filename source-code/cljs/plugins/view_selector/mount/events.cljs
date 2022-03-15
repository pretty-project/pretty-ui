
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.mount.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id body-props]]
  (assoc-in db [extension-id :view-selector/body-props] body-props))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :view-selector/body-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-did-mount body-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-will-unmount body-will-unmount)
