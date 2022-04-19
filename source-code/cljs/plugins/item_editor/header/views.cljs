
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.views
    (:require [plugins.item-editor.core.helpers   :as core.helpers]
              [plugins.item-editor.header.helpers :as header.helpers]
              [reagent.api                        :as reagent]
              [x.app-core.api                     :as a]
              [x.app-elements.api                 :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])]
       [elements/menu-bar ::header-menu-items
                          {:disabled?  editor-disabled?
                           :menu-items (header.helpers/header-menu-items editor-id)}]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  ; XXX#7080
  (if @(a/subscribe [:item-editor/data-received? editor-id])
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
