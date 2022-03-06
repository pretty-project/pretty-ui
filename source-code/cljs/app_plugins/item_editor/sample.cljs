
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.sample
    (:require [app-plugins.item-editor.api :as item-editor]
              [x.app-components.api        :as components]
              [x.app-core.api              :as a]
              [x.app-elements.api          :as elements]))



;; -- A plugin elindítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-editor plugin elindítható ...
(a/reg-event-fx
  :load-my-item-editor!
  (fn [_ _]
      ; ... az [:item-editor/edit-item! ...] esemény meghívásával.
      [:item-editor/edit-item! :my-extension :my-type "my-item"]
      ; ... az [:item-editor/load-editor! ...] esemény meghívásával.
      [:item-editor/load-editor! :my-extension :my-type {:item-id "my-item"}]
      ; ... az "/@app-home/my-extension/my-item" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension/my-item"]))



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  [surface-id]
  [:<> [item-editor/header :my-extension :my-type {}]
       [item-editor/body   :my-extension :my-type {:form-element [:div "My form"]}]])

(a/reg-event-fx
  :my-extension.my-type-editor/render-editor!
  [:ui/set-surface! :my-extension.my-type-editor/view
                    {:view #'my-view}])

; Az [:item-editor/load-editor! ...] esemény a [:my-extension.my-type-editor/load-editor!]
; esemény meghívásával fejezi be a plugin elindítását ...
(a/reg-event-fx
  :my-extension.my-type-editor/load-editor!
  [:my-extension.my-type-editor/render-editor!])



;; -- A plugin használata "Layout A" felületen --------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-editor/get-description :your-extension :your-type])]
       [layouts/layout-a surface-id {:header [item-editor/header :your-extension :your-type {}]
                                     :body   [item-editor/body   :your-extension :your-type {:form-element [:div "Your form"]}]
                                     :description description}]))



;; -- A :form-id tulajdonság használata ---------------------------------------
;; ----------------------------------------------------------------------------

; - A {:form-id ...} tulajdonság használatával ...
(defn our-type-form
  [extension-id item-namespace]
  [elements/text-field ::our-sample-field
                       {:form-id    (item-editor/form-id :our-extension :our-type)
                        :value-path [:our-extension :item-editor/data-items :our-key]}])

(defn our-view
  [surface-id]
  [item-editor/view :our-extension :our-type {:form-element #'our-type-form}])



;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------

; Ha az item-editor plugin {:auto-title? true} beállítással van használva, akkor a szerkesztő betöltésekor
; a plugin a megfelelő kifejezést beállítja az applikáció címének, ezért szükséges azokat hozzáadni a szótárhoz!
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:add-my-type  {:en "Add my type"  :hu "Típusom hozzáadása"}
                                         :edit-my-type {:en "Edit my type" :hu "Típusom szerkesztése"}}]})
