
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.subs
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ extension-id _ prop-key]]
  (get-in db [:plugins :item-editor/header-props extension-id prop-key]))

(defn get-body-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ extension-id _ prop-key]]
  (get-in db [:plugins :item-editor/body-props extension-id prop-key]))

(defn header-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id _]]
  (some? (get-in db [:plugins :item-editor/header-props extension-id])))

(defn body-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id _]]
  ; XXX#2045
  ; A {:handler-key ...} tulajdonság azért a szerver-oldali [:item-editor/init-editor! ...]
  ; esemény paraméterei között van, és nem a kliens-oldali body komponens paraméterei között, ...
  ; ... mert a body komponens React-fából történő lecsatolása után is szükséges a plugin számára,
  ;     hogy az egyes elemek törlésekor az item-editor plugin elhagyása után a "Törölt elem
  ;     visszaállítása" funkció hozzáférjen a {:handler-key ...} tulajdonság értékéhez,
  ;     ami nem maradhat a [:plugins :item-editor/body-props extension-id :handler-key] útvonalon,
  ;     mert a body-did-mount? feliratkozás tévesen azt érzékelné, hogy a body komponens a React-fába
  ;     van csatolva, ha adatok lennének ezen az útvonalon.
  (some? (get-in db [:plugins :item-editor/body-props extension-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/header-did-mount? header-did-mount?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/body-did-mount? body-did-mount?)
