
(ns app-plugins.item-editor.sample
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [app-plugins.item-editor.api :as item-editor]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.my-type-editor/how-to-start?
  ; Az item-editor plugin elindítható ...
  ; ... az [:item-editor/edit-item! ...] esemény meghívásával.
  [:item-editor/edit-item! :my-extension :my-type "my-item"]
  ; ... az [:item-editor/load-editor! ...] esemény meghívásával.
  [:item-editor/load-editor! :my-extension :my-type {:item-id "my-item"}]
  ; ... az "/@app-home/my-extension/my-item" útvonal használatával.
  [:router/go-to! "/@app-home/my-extension/my-item"])



;; -- Example A ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-editor plugint header és body komponensre felbontva is lehetséges használni



;; -- Example B ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-form-props
  [db _]
  {:my-key "My value"})

(a/reg-sub :my-extension.my-type-editor/get-form-props get-form-props)

(defn my-form-element-structure
  [body-id form-props]
  [:<> [elements/text-field ::my-sample-field
                            {:form-id    (item-editor/form-id :my-extension :my-type)
                             :value-path [:my-extension :item-editor/data-items :my-key]}]])

(defn my-form-element
  [extension-id item-namespace body-props]
  [components/subscriber {:base-props body-props
                          :render-f   #'my-form-element-structure
                          :subscriber [:my-extension.my-type-editor/get-form-props]}])

(defn my-view
  [surface-id]
  [item-editor/view :my-extension :my-type {:form-element #'my-form-element}])

(a/reg-event-fx
  :my-extension.my-type-editor/load-editor!
  [:ui/set-surface! :my-extension.my-type-editor/view
                    {:view #'my-view}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:add-my-type {:en "Add my type"
                                                       :hu "Típusom hozzáadása"}
                                         :edit-my-type {:en "Edit my type"
                                                        :hu "Típusom szerkesztése"}}]})
