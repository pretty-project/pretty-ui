
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
  (assoc-in db [:plugins :view-selector/body-props extension-id] body-props))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [:plugins :view-selector/body-props extension-id])
         (dissoc-in [:plugins :view-selector/meta-items extension-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-did-mount body-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-will-unmount body-will-unmount)
