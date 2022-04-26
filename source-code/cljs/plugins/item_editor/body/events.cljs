
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.vector                  :as vector]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.events    :as core.events]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.body.events :as body.events]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.events
(def store-body-props!  body.events/store-body-props!)
(def remove-body-props! body.events/remove-body-props!)
(def update-item-id!    body.events/update-item-id!)
(def update-view-id!    body.events/update-view-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:initial-item (map)(opt)
  ;   :item-path (vector)}
  ;
  ; @return (map)
  [db [_ editor-id {:keys [initial-item item-path] :as body-props}]]
  ; Az item-editor plugin body komponensének ...
  ; ... {:initial-item {...}} tulajdonságával megadható a szerkesztett dokumentum kezdeti állapota ...
  ;     ... így elkerülhető az input mezők {:initial-value ...} tulajdonságának használata, ami miatt
  ;         a plugin felhasználói változtatás nélküli elhagyásakor az tévesen felajánlaná
  ;         a "Nem mentett változtatások visszaállítása" lehetőséget.
  ;     ... így beállíthatók a dokumentum felhasználó által nem szerkeszthető tulajdonságai.
  (cond-> db :store-body-props! (as-> % (r store-body-props! % editor-id body-props))
             :update-view-id!   (as-> % (r update-view-id!   % editor-id))
             :update-item-id!   (as-> % (r update-item-id!   % editor-id))
             initial-item       (assoc-in item-path initial-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r core.events/remove-meta-items! % editor-id)
             (r core.events/reset-downloads!   % editor-id)
             (r remove-body-props!             % editor-id)))
