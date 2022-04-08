
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.header.views
    (:require [mid-fruits.logical                 :refer [nor]]
              [mid-fruits.vector                  :as vector]
              [plugins.item-lister.core.helpers   :as core.helpers]
              [plugins.item-lister.header.helpers :as header.helpers]
              [react.api                          :as react]
              [reagent.api                        :as reagent]
              [x.app-core.api                     :as a]
              [x.app-elements.api                 :as elements]))



;; -- Search-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-search-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-search-mode! lister-id]}]))

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/search-field :item-lister/search-items-field
                              {:auto-focus? true :min-width :xs :placeholder :search
                               :disabled?     (or error-mode? lister-disabled?)
                               :indent        {:top :xxs :right :xxs}
                               :on-empty      [:item-lister/search-items!          lister-id]
                               :on-type-ended [:item-lister/search-items!          lister-id]
                               :value-path    [:plugins :plugin-handler/meta-items lister-id :search-term]}]))

(defn toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/toggle-search-mode-button
                             {:preset :search :tooltip :search
                              :disabled? (or error-mode? lister-disabled?)
                              :on-click  [:item-lister/toggle-search-mode! lister-id]}]))

(defn search-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/search-block :my-lister]
  [lister-id]
  (let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
       (if viewport-small? [toggle-search-mode-button lister-id]
                           [search-items-field        lister-id])))



;; -- Select-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-select-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-select-mode! lister-id]}]))


(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/unselect-all-items-button
                             {:icon :check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! lister-id]}]))

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/unselect-some-items-button
                             {:icon :indeterminate_check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! lister-id]}]))

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/select-all-items-button
                             {:icon :check_box_outline_blank :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/select-all-items! lister-id]}]))

(defn toggle-all-items-selection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [all-items-selected? @(a/subscribe [:item-lister/all-items-selected? lister-id])
        any-item-selected?  @(a/subscribe [:item-lister/any-item-selected?  lister-id])]
       (cond all-items-selected? [unselect-all-items-button  lister-id]
             any-item-selected?  [unselect-some-items-button lister-id]
             :no-items-selected  [select-all-items-button    lister-id])))

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      lister-id :item-actions])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       (if (vector/contains-item? item-actions :delete)
           [elements/icon-button :item-lister/delete-selected-items-button
                                 {:preset :delete :tooltip :delete!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/delete-selected-items! lister-id]}])))

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      lister-id :item-actions])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       (if (vector/contains-item? item-actions :duplicate)
           [elements/icon-button :item-lister/duplicate-selected-items-button
                                 {:preset :duplicate :tooltip :duplicate!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/duplicate-selected-items! lister-id]}])))



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-reorder-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-reorder-mode! lister-id]}]))
(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])
        order-changed?   @(a/subscribe [:item-lister/order-changed?   lister-id])]
       [elements/button :item-lister/save-order-button
                        {:label :save-order! :preset :primary-button
                         :disabled? (or lister-disabled? (not order-changed?))
                         :on-click  [:item-lister/save-order! lister-id]}]))



;; -- Menu-mode header components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/new-item-button :my-lister]
  [lister-id]
  (if-let [new-item-event @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-event])]
          (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
                lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
               (if-let [new-item-options @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-options])]
                       [elements/select :item-lister/new-item-select
                                        {:as-button? true :autoclear? true :icon :add_circle :preset :primary-icon-button :tooltip :add-new!
                                         :initial-options new-item-options
                                         :on-select       new-item-event
                                         :disabled?       (or error-mode? lister-disabled?)}]
                       [elements/icon-button :item-lister/new-item-button
                                             {:icon :add_circle :preset :primary :tooltip :add-new!
                                              :on-click  new-item-event
                                              :disabled? (or error-mode? lister-disabled?)}]))))


(defn toggle-select-mode-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/toggle-select-mode-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        items-selectable? @(a/subscribe [:item-lister/items-selectable? lister-id])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       (if items-selectable? [elements/icon-button :item-lister/toggle-select-mode-button
                                                   {:preset :select-mode :tooltip :select
                                                    :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                    :on-click  [:item-lister/toggle-select-mode! lister-id]}])))

(defn toggle-reorder-mode-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        items-sortable?   @(a/subscribe [:item-lister/get-body-prop     lister-id :sortable?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       (if items-sortable? [elements/icon-button :item-lister/toggle-reorder-mode-button
                                                 {:preset :reorder-mode :tooltip :reorder
                                                  :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                  :on-click  [:item-lister/toggle-reorder-mode! lister-id]}])))

(defn sort-items-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/select :item-lister/sort-items-button
                        {:as-button? true :options-label :order-by :preset :order-by-icon-button :tooltip :order-by
                         :disabled?    (or error-mode? lister-disabled? no-items-to-show?)
                         :on-select    [:item-lister/order-items!           lister-id]
                         :options-path [:plugins :plugin-handler/body-props lister-id :order-by-options]
                         :value-path   [:plugins :plugin-handler/meta-items lister-id :order-by]
                         :get-label-f  header.helpers/order-by-label-f}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar [search-items-field      lister-id]
                                      [quit-search-mode-button lister-id]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [search-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                              [search-mode-header-structure lister-id]]))

(defn select-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button lister-id]
      [delete-selected-items-button      lister-id]
      [duplicate-selected-items-button   lister-id]]
    [quit-select-mode-button lister-id]])

(defn select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [select-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :select-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                              [select-mode-header-structure lister-id]]))

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [new-item-button            lister-id]
                                               [sort-items-button          lister-id]
                                               [toggle-select-mode-button  lister-id]
                                               [toggle-reorder-mode-button lister-id]]
    [:div.item-lister--header--menu-item-group [search-block lister-id]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :reorder-mode?])
        search-mode?  @(a/subscribe [:item-lister/get-meta-item lister-id :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? (nor reorder-mode? search-mode?)}
                              (if-let [menu-element @(a/subscribe [:item-lister/get-header-prop lister-id :menu-element])]
                                      [menu-element               lister-id]
                                      [menu-mode-header-structure lister-id])]))
(defn reorder-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [elements/button {:layout :icon-button :variant :placeholder}]]
    [:div.item-lister--header--menu-item-group [save-order-button lister-id]]
    [quit-reorder-mode-button lister-id]])

(defn reorder-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :reorder-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                              [reorder-mode-header-structure lister-id]]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (if @(a/subscribe [:item-lister/header-did-mount? lister-id])
       [:div#item-lister--header--structure [menu-mode-header    lister-id]
                                            [reorder-mode-header lister-id]
                                            [select-mode-header  lister-id]
                                            [search-mode-header  lister-id]]))

(defn header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:menu-element (metamorphic-content)(opt)
  ;   :new-item-event (metamorphic-event)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/header :my-lister {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [lister-id] [:div ...])
  ;  [item-lister/header :my-lister {:menu #'my-menu-element}}]
  [lister-id header-props]
  (reagent/lifecycles (core.helpers/component-id lister-id :header)
                      {:reagent-render         (fn []             [header-structure                 lister-id])
                       :component-did-mount    (fn [] (a/dispatch [:item-lister/header-did-mount    lister-id header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-lister/header-will-unmount lister-id]))}))
