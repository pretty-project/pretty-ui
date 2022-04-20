
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::quit-search-mode-icon-button
                             {:disabled? lister-disabled?
                              :keypress  {:key-code 27}
                              :on-click  [:item-lister/quit-search-mode! lister-id]
                              :preset    :close}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; - XXX#6779
  ;
  ; - A search-items-field mező bal oldalán is szükséges {:indent ...} távolságot beállítani,
  ;   mert kis méretű képernyőkön a mező a képernyő bal oldalán jelenik meg.
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/search-field ::search-items-field
                              {:auto-focus?   true
                               :disabled?     (or error-mode? lister-disabled?)
                               :min-width     :xs
                               :placeholder   :search
                               :indent        {:all :xxs}
                               :on-empty      [:item-lister/search-items!          lister-id]
                               :on-type-ended [:item-lister/search-items!          lister-id]
                               :value-path    [:plugins :plugin-handler/meta-items lister-id :search-term]}]))

(defn set-search-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::set-search-mode-icon-button
                             {:disabled? (or error-mode? lister-disabled?)
                              :on-click  [:item-lister/set-search-mode! lister-id]
                              :preset    :search}]))

(defn search-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/search-block :my-lister]
  [lister-id]
  (let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
       (if viewport-small? [set-search-mode-icon-button lister-id]
                           [search-items-field          lister-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-actions-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::quit-actions-mode-icon-button
                             {:disabled? lister-disabled?
                              :keypress  {:key-code 27}
                              :on-click  [:item-lister/quit-actions-mode! lister-id]
                              :preset    :close}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unselect-all-items-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::unselect-all-items-icon-button
                             {:disabled? lister-disabled?
                              :icon      :check_box
                              :on-click  [:item-lister/reset-selections! lister-id]
                              :preset    :default}]))

(defn unselect-some-items-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::unselect-some-items-icon-button
                             {:disabled? lister-disabled?
                              :icon      :indeterminate_check_box
                              :on-click  [:item-lister/reset-selections! lister-id]
                              :preset    :default}]))

(defn select-all-items-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::select-all-items-icon-button
                             {:disabled? lister-disabled?
                              :icon      :check_box_outline_blank
                              :on-click  [:item-lister/select-all-items! lister-id]
                              :preset    :default}]))

(defn toggle-all-items-selection-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [all-items-selected? @(a/subscribe [:item-lister/all-items-selected? lister-id])
        any-item-selected?  @(a/subscribe [:item-lister/any-item-selected?  lister-id])]
       (cond all-items-selected? [unselect-all-items-icon-button  lister-id]
             any-item-selected?  [unselect-some-items-icon-button lister-id]
             :no-items-selected  [select-all-items-icon-button    lister-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-selected-items-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       [elements/icon-button ::delete-selected-items-icon-button
                             {:disabled? (or lister-disabled? no-items-selected?)
                              :on-click  [:item-lister/delete-selected-items! lister-id]
                              :preset    :delete}]))

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       [elements/button ::delete-selected-items-button
                        {:disabled?   (or lister-disabled? no-items-selected?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-lister/delete-selected-items! lister-id]
                         :preset      :delete}]))

(defn delete-selected-items-block
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions @(a/subscribe [:item-lister/get-header-prop lister-id :item-actions])]
       (if (vector/contains-item? item-actions :delete)
           (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
                   [delete-selected-items-icon-button lister-id]
                   [delete-selected-items-button      lister-id]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-selected-items-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       [elements/icon-button ::duplicate-selected-items-icon-button
                             {:disabled? (or lister-disabled? no-items-selected?)
                              :on-click  [:item-lister/duplicate-selected-items! lister-id]
                              :preset    :duplicate}]))

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       [elements/button ::duplicate-selected-items-button
                        {:disabled?   (or lister-disabled? no-items-selected?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-lister/duplicate-selected-items! lister-id]
                         :preset      :duplicate}]))

(defn duplicate-selected-items-block
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions @(a/subscribe [:item-lister/get-header-prop lister-id :item-actions])]
       (if (vector/contains-item? item-actions :duplicate)
           (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
                   [duplicate-selected-items-icon-button lister-id]
                   [duplicate-selected-items-button      lister-id]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-reorder-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::quit-reorder-mode-icon-button
                             {:disabled? lister-disabled?
                              :keypress  {:key-code 27}
                              :on-click  [:item-lister/quit-reorder-mode! lister-id]
                              :preset    :close}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])
        order-changed?   @(a/subscribe [:item-lister/order-changed?   lister-id])]
       [elements/button ::save-order-button
                        {:disabled? (or lister-disabled? (not order-changed?))
                         :font-size :xs
                         :indent    {:horizontal :xxs :vertical :xs}
                         :label     :save-order!
                         :on-click  [:item-lister/save-order! lister-id]
                         :preset    :primary}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-icon-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [new-item-event   @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-event])
        new-item-options @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-options])
        error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/select ::new-item-icon-select
                        {:autoclear?      true
                         :disabled?       (or error-mode? lister-disabled?)
                         :icon            :add_circle
                         :layout          :icon-button
                         :preset          :primary
                         :initial-options new-item-options
                         :on-select       new-item-event}]))

(defn new-item-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [new-item-event   @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-event])
        new-item-options @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-options])
        error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/select ::new-item-select
                        {:autoclear?      true
                         :disabled?       (or error-mode? lister-disabled?)
                         :font-size       :xs
                         :hover-color     :highlight
                         :indent          {:horizontal :xxs :left :xxs}
                         :label           :add-new!
                         :layout          :button
                         :preset          :primary
                         :initial-options new-item-options
                         :on-select       new-item-event}]))

(defn new-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [new-item-event   @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-event])
        error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button ::new-item-icon-button
                             {:disabled? (or error-mode? lister-disabled?)
                              :on-click  new-item-event
                              :preset    :add}]))

(defn new-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [new-item-event   @(a/subscribe [:item-lister/get-header-prop  lister-id :new-item-event])
        error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/button ::new-item-button
                        {:disabled?   (or error-mode? lister-disabled?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :label       :add-new!
                         :on-click    new-item-event
                         :preset      :primary}]))

(defn new-item-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/new-item-block :my-lister]
  [lister-id]
  (if-let [new-item-event @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-event])]
          (let [new-item-options @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-options])
                viewport-small?  @(a/subscribe [:environment/viewport-small?])]
               (cond (and new-item-options viewport-small?) [new-item-icon-select lister-id]
                          new-item-options                  [new-item-select      lister-id]
                                           viewport-small?  [new-item-icon-button lister-id]
                                           :else            [new-item-button      lister-id]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-actions-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
      [elements/icon-button ::set-actions-mode-icon-button
                            {:color     :default
                             :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                             :on-click  [:item-lister/set-actions-mode! lister-id]
                             :preset    :select}]))

(defn set-actions-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
      [elements/button ::set-actions-mode-button
                       {:color       :default
                        :disabled?   (or error-mode? lister-disabled? no-items-to-show?)
                        :font-size   :xs
                        :hover-color :highlight
                        :indent      {:horizontal :xxs :left :xxs}
                        :on-click    [:item-lister/set-actions-mode! lister-id]
                        :preset      :select}]))

(defn set-actions-mode-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/set-actions-mode-block :my-lister]
  [lister-id]
  (if-let [items-actions @(a/subscribe [:item-lister/get-header-prop lister-id :item-actions])]
          (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
                  [set-actions-mode-icon-button lister-id]
                  [set-actions-mode-button      lister-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-reorder-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/icon-button ::toggle-reorder-mode-icon-button
                             {:disabled? (or error-mode? lister-disabled? no-items-to-show?)
                              :on-click  [:item-lister/set-reorder-mode! lister-id]
                              :preset    :reorder}]))

(defn toggle-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/button ::toggle-reorder-mode-button
                        {:disabled?   (or error-mode? lister-disabled? no-items-to-show?)
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:horizontal :xxs :left :xxs}
                         :on-click    [:item-lister/set-reorder-mode! lister-id]
                         :preset      :reorder}]))

(defn toggle-reorder-mode-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-block :my-lister]
  [lister-id]
  (if-let [items-sortable? @(a/subscribe [:item-lister/get-body-prop lister-id :sortable?])]
          (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
                  [toggle-reorder-mode-icon-button lister-id]
                  [toggle-reorder-mode-button      lister-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sort-items-icon-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/select ::sort-items-icon-select
                        {:disabled?     (or error-mode? lister-disabled? no-items-to-show?)
                         :get-label-f   header.helpers/order-by-label-f
                         :layout        :icon-button
                         :on-select     [:item-lister/order-items! lister-id]
                         :options-label :order-by
                         :options-path  [:plugins :plugin-handler/body-props lister-id :order-by-options]
                         :preset        :order-by
                         :value-path    [:plugins :plugin-handler/meta-items lister-id :order-by]}]))

(defn sort-items-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/select ::sort-items-select
                        {:disabled?     (or error-mode? lister-disabled? no-items-to-show?)
                         :font-size     :xs
                         :get-label-f   header.helpers/order-by-label-f
                         :hover-color   :highlight
                         :indent        {:horizontal :xxs :left :xxs}
                         :layout        :button
                         :on-select     [:item-lister/order-items! lister-id]
                         :options-label :order-by
                         :options-path  [:plugins :plugin-handler/body-props lister-id :order-by-options]
                         :preset        :order-by
                         :value-path    [:plugins :plugin-handler/meta-items lister-id :order-by]}]))

(defn sort-items-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/sort-items-block :my-lister]
  [lister-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [sort-items-icon-select lister-id]
          [sort-items-select      lister-id]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar [search-items-field           lister-id]
                                      [quit-search-mode-icon-button lister-id]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [search-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                              [search-mode-header-structure lister-id]]))

(defn actions-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [toggle-all-items-selection-icon-button lister-id]
                                               [delete-selected-items-block            lister-id]
                                               [duplicate-selected-items-block         lister-id]]
    [quit-actions-mode-icon-button lister-id]])

(defn actions-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [actions-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :actions-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? actions-mode?}
                              [actions-mode-header-structure lister-id]]))

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [new-item-block            lister-id]
                                               [sort-items-block          lister-id]
                                               [set-actions-mode-block    lister-id]
                                               [toggle-reorder-mode-block lister-id]]
    [:div.item-lister--header--menu-item-group [search-block              lister-id]]])

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
    [:div.item-lister--header--menu-item-group [elements/icon-button {:variant :placeholder}]]
    [:div.item-lister--header--menu-item-group [save-order-button lister-id]]
    [quit-reorder-mode-icon-button lister-id]])

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
  ; - BUG#0041
  ;   Az #item-lister--header-placeholder elem biztosítja a header komponens magasságát,
  ;   annak tartalmának megjelenéséig.
  ;   Az #item-lister--header-placeholder elem használata nélkül a header komponens alatt
  ;   elhelyezett elemek (pl. vízszintes elválasztó vonal, body komponens, ...) helyzete
  ;   megváltozna a header komponens tartalmának megjelenésekor.
  ;
  ; - XXX#7080
  ;   A header komponens tartalma az x4.7.0 verzióig az [:item-lister/header-props-stored? ...]
  ;   feliratkozás kimenetétől függően jelent meg.
  ;   Az [:item-lister/items-received? ...] feliratkozás használatával az item-lister plugin
  ;   betöltésekor a header komponens tartalma egy időben jelenik meg, a letöltésjelző eltűnésével
  ;   és a listaelemek megjelenésével.
  (if-let [items-received? @(a/subscribe [:item-lister/items-received? lister-id])]
          [:div.item-lister--header-structure [menu-mode-header    lister-id]
                                              [reorder-mode-header lister-id]
                                              [actions-mode-header lister-id]
                                              [search-mode-header  lister-id]]))
         ;[:div.item-lister--header-placeholder]

(defn header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :menu-element (metamorphic-content)(opt)
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
