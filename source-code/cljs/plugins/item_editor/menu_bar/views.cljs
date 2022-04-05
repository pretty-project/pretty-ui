
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.menu-bar.views
    (:require [plugins.item-editor.core.helpers     :as core.helpers]
              [plugins.item-editor.menu-bar.helpers :as menu-bar.helpers]
              [reagent.api                          :as reagent]
              [x.app-core.api                       :as a]
              [x.app-elements.api                   :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/menu-bar ::menu-bar
                     {:menu-items (menu-bar.helpers/header-menu-items editor-id)}])

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if @(a/subscribe [:item-editor/header-did-mount? editor-id])
       [header-menu-items editor-id]))

(defn header
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:menu-items (maps in vector)
  ;    [{:change-keys (keywords in vector)
  ;      :label (metamorphic-content)
  ;      :view-id (keyword)}]}
  ;
  ; @usage
  ;  [item-editor/header :my-editor {...}]
  [editor-id header-props]
  (reagent/lifecycles (core.helpers/component-id editor-id :header)
                      {:reagent-render         (fn []             [header-structure                 editor-id])
                       :component-did-mount    (fn [] (a/dispatch [:item-editor/header-did-mount    editor-id header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-editor/header-will-unmount editor-id]))}))
