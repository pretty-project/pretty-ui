
(ns app-plugins.item-editor.sample
    (:require [x.app-core.api :as a]
              [app-plugins.item-editor.api :as item-editor]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :open-item-editor!
  [:router/go-to! (item-editor/editor-uri :my-extension :my-type "my-item-id")])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  [body-id body-props]
  [:<> [elements/field ::my-sample-field
                       {:form-id    (item-editor/form-id :my-extension :my-type)
                        :value-path [:my-extension :item-editor/data-items :my-value]}]])

(defn body
  [body-id]
  (let [body-props (a/subscribe [:item-editor/get-body-props :my-extension :my-type])]
       (fn [] [body-structure body-id @body-props])))

(defn view
  [surface-id]
  [item-editor/view :my-extension :my-type {:form-element #'body}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :my-extension/render-my-type-editor! [:ui/set-surface! ::view {:view {:content #'view}}])
