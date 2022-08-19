

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.footer.views
    (:require [plugins.item-editor.core.helpers :as core.helpers]
              [reagent.api                      :as reagent]
              [x.app-core.api                   :as a]
              [x.app-elements.api               :as elements]))



;; -- Revert item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- revert-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        item-changed?    @(a/subscribe [:item-editor/item-changed?    editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/icon-button ::revert-item-icon-button
                             {:disabled? (or editor-disabled? error-mode? (not item-changed?))
                              :on-click  [:item-editor/revert-item! editor-id]
                              :preset    :restore}]))

(defn- revert-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        item-changed?    @(a/subscribe [:item-editor/item-changed?    editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/button ::revert-item-button
                        {:disabled?   (or editor-disabled? error-mode? (not item-changed?))
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :right :xxs}
                         :preset      :restore
                         :on-click    [:item-editor/revert-item! editor-id]}]))

(defn revert-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/revert-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [revert-item-icon-button editor-id]
          [revert-item-button      editor-id]))



;; -- Cancel item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/icon-button ::cancel-item-icon-button
                             {:disabled? (or editor-disabled? error-mode?)
                              :on-click  [:item-editor/cancel-item! editor-id]
                              :preset    :cancel}]))

(defn- cancel-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/button ::cancel-item-button
                        {:disabled?   (or editor-disabled? error-mode?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-editor/cancel-item! editor-id]
                         :preset      :cancel}]))

(defn cancel-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/cancel-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [cancel-item-icon-button editor-id]
          [cancel-item-button      editor-id]))



;; -- Save item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  editor-id])]
       [elements/icon-button ::save-item-icon-button
                             {:disabled? (or editor-disabled? error-mode? (not form-completed?))
                              :on-click  [:item-editor/save-item! editor-id]
                              :preset    :save}]))

(defn- save-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  editor-id])]
       [elements/button ::save-item-button
                        {:disabled?   (or editor-disabled? error-mode? (not form-completed?))
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :right :xxs}
                         :on-click    [:item-editor/save-item! editor-id]
                         :preset      :save}]))

(defn save-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/save-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [save-item-icon-button editor-id]
          [save-item-button      editor-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [cancel-item-block editor-id])

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  ; XXX#0455
  ; "Új elem hozzáadása" módban a "Visszaállítás" gomb nem jelenik meg, mivel nem számít
  ; releváns információnak a dokumentum megnyitáskori (üres) állapota.
  (if-let [new-item? @(a/subscribe [:item-editor/new-item? editor-id])]
          [:<> [save-item-block   editor-id]]
          [:<> [revert-item-block editor-id]
               [save-item-block   editor-id]]))

(defn menu-mode-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/horizontal-polarity ::menu-mode-footer
                                {:start-content [menu-start-buttons editor-id]
                                 :end-content   [menu-end-buttons   editor-id]}])

(defn- footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  ; XXX#7080
  (if @(a/subscribe [:item-editor/data-received? editor-id])
       (if-let [menu-element @(a/subscribe [:item-editor/get-footer-prop editor-id :menu-element])]
               [menu-element     editor-id]
               [menu-mode-footer editor-id])))

(defn footer
  ; @param (keyword) editor-id
  ; @param (map) footer-props
  ;  {:menu-element (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/footer :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [editor-id] [:div ...])
  ;  [item-editor/footer :my-editor {:menu #'my-menu-element}]
  [editor-id footer-props]
  (reagent/lifecycles (core.helpers/component-id editor-id :footer)
                      {:reagent-render         (fn []              [footer-structure                 editor-id])
                       :component-did-mount    (fn []  (a/dispatch [:item-editor/footer-did-mount    editor-id footer-props]))
                       :component-will-unmount (fn []  (a/dispatch [:item-editor/footer-will-unmount editor-id]))
                       :component-did-update   (fn [%] (a/dispatch [:item-lister/footer-did-update   editor-id %]))}))
