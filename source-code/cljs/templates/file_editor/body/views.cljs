
(ns templates.file-editor.body.views
    (:require [components.api          :as components]
              [engines.file-editor.api :as file-editor]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-editor
  ; @param (keyword) editor-id
  ; @param (map) body-props
  [editor-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:content :the-item-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :box-surface-body}])]
       [file-editor/body editor-id body-props]))

(defn body
  ; A komponens további paraméterezését az engines.file-editor.api/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ; {:form-element (component or symbol)}
  ;
  ; @usage
  ; [body :my-editor {...}]
  ;
  ; @usage
  ; (defn my-form-element [] ...)
  ; [body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [] ; body-props (item-editor.prototypes/body-props-prototype editor-id body-props)
       [:div#t-file-editor--body [file-editor editor-id body-props]]))
