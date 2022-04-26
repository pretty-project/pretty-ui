
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.footer.views
    (:require [mid-fruits.vector                :as vector]
              [plugins.item-viewer.core.helpers :as core.helpers]
              [reagent.api                      :as reagent]
              [x.app-core.api                   :as a]
              [x.app-elements.api               :as elements]))



;; -- Delete item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delete-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/icon-button ::delete-item-icon-button
                             {:disabled? (or viewer-disabled? error-mode?)
                              :on-click  [:item-viewer/delete-item! viewer-id]
                              :preset    :delete}]))

(defn- delete-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/button ::delete-item-button
                        {:disabled?   (or viewer-disabled? error-mode?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-viewer/delete-item! viewer-id]
                         :preset      :delete}]))

(defn delete-item-block
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [item-viewer/delete-item-block :my-viewer]
  [viewer-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [delete-item-icon-button viewer-id]
          [delete-item-button      viewer-id]))



;; -- Copy item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/icon-button ::duplicate-item-icon-button
                             {:disabled? (or viewer-disabled? error-mode?)
                              :on-click  [:item-viewer/duplicate-item! viewer-id]
                              :preset    :duplicate}]))

(defn- duplicate-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/button ::duplicate-item-button
                        {:disabled?   (or viewer-disabled? error-mode?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-viewer/duplicate-item! viewer-id]
                         :preset      :duplicate}]))

(defn duplicate-item-block
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [item-viewer/copy-item-block :my-viewer]
  [viewer-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [duplicate-item-icon-button viewer-id]
          [duplicate-item-button      viewer-id]))



;; -- Edit item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- edit-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/icon-button ::edit-item-icon-button
                             {:disabled? (or viewer-disabled? error-mode?)
                              :on-click  [:item-viewer/edit-item! viewer-id]
                              :preset    :edit}]))

(defn- edit-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? viewer-id])
        error-mode?      @(a/subscribe [:item-viewer/get-meta-item    viewer-id :error-mode?])]
       [elements/button ::save-item-button
                        {:disabled?   (or viewer-disabled? error-mode?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :right :xxs}
                         :on-click    [:item-viewer/edit-item! viewer-id]
                         :preset      :edit}]))

(defn edit-item-block
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [item-viewer/edit-item-block :my-viewer]
  [viewer-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [edit-item-icon-button viewer-id]
          [edit-item-button      viewer-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [item-actions @(a/subscribe [:item-viewer/get-body-prop viewer-id :item-actions])]
       [:<> (if (vector/contains-item? item-actions :delete)    [delete-item-block    viewer-id])
            (if (vector/contains-item? item-actions :duplicate) [duplicate-item-block viewer-id])]))

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [item-actions @(a/subscribe [:item-viewer/get-body-prop viewer-id :item-actions])]
       (if (vector/contains-item? item-actions :edit) [edit-item-block viewer-id])))

(defn menu-mode-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  [elements/horizontal-polarity ::menu-mode-footer
                                {:start-content [menu-start-buttons viewer-id]
                                 :end-content   [menu-end-buttons   viewer-id]}])

(defn- footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  ; XXX#7080
  (if @(a/subscribe [:item-viewer/data-received? viewer-id])
       (if-let [menu-element @(a/subscribe [:item-viewer/get-footer-prop viewer-id :menu-element])]
               [menu-element     viewer-id]
               [menu-mode-footer viewer-id])))

(defn footer
  ; @param (keyword) viewer-id
  ; @param (map) footer-props
  ;  {:menu-element (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-viewer/footer :my-viewer {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [viewer-id] [:div ...])
  ;  [item-viewer/footer :my-viewer {:menu #'my-menu-element}]
  [viewer-id footer-props]
  (reagent/lifecycles (core.helpers/component-id viewer-id :footer)
                      {:reagent-render         (fn []             [footer-structure                 viewer-id])
                       :component-did-mount    (fn [] (a/dispatch [:item-viewer/footer-did-mount    viewer-id footer-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-viewer/footer-will-unmount viewer-id]))}))
