
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.form.subs
    (:require [mid-fruits.candy               :refer [return]]
              [plugins.item-editor.mount.subs :as mount.subs]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-elements.api             :as elements]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/form-completed? :my-editor]
(a/reg-sub :item-editor/form-completed? form-completed?)
