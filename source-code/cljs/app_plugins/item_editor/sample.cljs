
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.sample
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [app-plugins.item-editor.api :as item-editor]))



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



;; -- A :form-id tulajdonság használata ---------------------------------------
;; ----------------------------------------------------------------------------

; - A {:form-id ...} tulajdonság használatával ...
(defn your-type-form
  []
  [elements/text-field ::your-sample-field
                       {:form-id    (item-editor/form-id :your-extension :your-type)
                        :value-path [:my-extension :item-editor/data-items :your-key]}])

(defn your-view
  [surface-id]
  [item-editor/view :your-extension :your-type {:form-element #'your-type-form}])



;; -- Kifejezések hozzáadása a szótárhoz --------------------------------------
;; ----------------------------------------------------------------------------

; Ha az item-editor plugin {:routed? true} beállítással van használva, akkor az útvonal betöltésekor
; a plugin a megfelelő kifejezést beállítja az applikáció címének, ezért szükséges azt hozzáadni a szótárhoz!
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:add-my-type {:en "Add my type"
                                                       :hu "Típusom hozzáadása"}
                                         :edit-my-type {:en "Edit my type"
                                                        :hu "Típusom szerkesztése"}}]})
