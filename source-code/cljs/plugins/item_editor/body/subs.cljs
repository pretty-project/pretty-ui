
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.subs
    (:require [mid-fruits.candy                 :refer [return]]
              [plugins.plugin-handler.body.subs :as body.subs]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.api               :as elements]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.subs
(def get-body-prop   body.subs/get-body-prop)
(def body-did-mount? body.subs/body-did-mount?)



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
  (if-let [form-id (r get-body-prop db editor-id :form-id)]
          (r elements/form-completed? db form-id)
          (return true)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/form-completed? :my-editor]
(a/reg-sub :item-editor/form-completed? form-completed?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-body-prop get-body-prop)
