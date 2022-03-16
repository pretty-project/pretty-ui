
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/form-completed? db extension-id item-namespace)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (if-let [form-id (r mount.subs/get-body-prop db extension-id item-namespace :form-id)]
          (r elements/form-completed? db form-id)
          (return true)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/form-completed? :my-extension :my-type]
(a/reg-sub :item-editor/form-completed? form-completed?)
