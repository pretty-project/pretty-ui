
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.subs
    (:require [mid-fruits.candy                :refer [return]]
              [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.mount.subs  :as mount.subs]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-elements.api              :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-completed?
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  (r item-editor/form-completed? db :my-editor)
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (if-let [form-id (r mount.subs/get-body-prop db editor-id :form-id)]
          (r elements/form-completed? db form-id)
          (return true)))

(defn form-changed?
  ; @param (keyword) editor-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r item-editor/form-changed? db :my-editor [:name :email-address])
  ;
  ; @return (boolean)
  [db [_ editor-id change-keys]]
  ; - A form-changed? függvény összehasonlítja az elem {:change-keys [...]} paraméterként
  ;   átadott kulcsainak értékeit az elemről tárolt másolat értékeivel.
  ;
  ; - Az egyes értékek összehasonlítását csak akkor végzi el, ha az adott érték nem üres!
  ;   Ha egy érték pl. nil, üres string vagy üres vektor, akkor nem hasonlítja össze a tárolt
  ;   másolat azonos kulcsú értékével!
  ;   Pl.: Az egyes input mezők használatakor ha a felhasználó kiüríti a mezőt,
  ;        akkor a visszamaradó üres string értéket a mező használata előtti nil értékkel
  ;        összehasonlítva különbségként érzékelné!
  (boolean (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                 current-item    (r core.subs/get-current-item    db editor-id)
                 backup-item     (r backup.subs/get-backup-item   db editor-id current-item-id)]
                (letfn [(f [change-key] (if (-> current-item change-key empty? not)
                                            (not= (change-key current-item)
                                                  (change-key backup-item))))]
                       (some f change-keys)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/form-completed? :my-editor]
(a/reg-sub :item-editor/form-completed? form-completed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:item-editor/form-completed? :my-editor [:name :email-address]]
(a/reg-sub :item-editor/form-changed? form-changed?)
