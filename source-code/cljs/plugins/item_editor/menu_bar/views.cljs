
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.menu-bar.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;  {:menu-items (maps in vector)
  ;    [{:change-keys (keywords in vector)
  ;      :label (metamorphic-content)
  ;      :view-id (keyword)}]}
  ;
  ; @usage
  ;  [item-editor/header :my-editor {...}]
  [editor-id bar-props]
  (let [selected-view-id @(a/subscribe [:gestures/get-selected-view-id :models.model-editor/view-handler])]
       (letfn [(f [label view-id] (let [on-click [:gestures/change-view! :models.model-editor/view-handler view-id]]
                                       {:label label :on-click on-click :active? (= view-id selected-view-id)}))]
              [elements/menu-bar ::menu-bar {:menu-items [(f "Adatok"  :data)
                                                          (f "Képek"   :images)
                                                          (f "Típusok" :types)]}])))
