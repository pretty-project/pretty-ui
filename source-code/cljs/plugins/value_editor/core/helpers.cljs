
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-edit-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (item-path vector)
  [extension-id editor-id]
  [extension-id :value-editor/data-items editor-id])

(defn view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; (core.helpers/view-id :my-extension :my-editor)
  ; =>
  ; :my-extension.my-editor/view
  ;
  ; @return (keyword)
  [extension-id editor-id]
  (keyword (str (name extension-id) "."
                (name editor-id))
           "view"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :min-width (keyword)
  ;   :value-path (item-path vector)}
  [extension-id editor-id]
  (let [editor-props @(a/subscribe [:value-editor/get-editor-props extension-id editor-id])]
       (merge (select-keys editor-props [:initial-value :label :modifier :validator])
              {:auto-focus? true
               :min-width   :l
               :value-path  (:edit-path editor-props)})))
