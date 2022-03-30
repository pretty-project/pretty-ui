
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.views
    (:require [extensions.storage.core.config         :as core.config]
              [extensions.storage.media-browser.views :as media-browser.views]
              [plugins.item-browser.api               :as item-browser]
              [plugins.item-browser.core.views        :as plugins.item-browser.core.views]
              [plugins.item-lister.core.views         :as plugins.item-lister.core.views]
              [mid-fruits.css                         :as css]
              [mid-fruits.keyword                     :as keyword]
              [x.app-core.api                         :as a]
              [x.app-elements.api                     :as elements]
              [x.app-media.api                        :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; extensions.storage.media-browser.views
(def directory-item media-browser.views/directory-item)
(def file-item      media-browser.views/file-item)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! :storage.media-picker/view]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-picker])]
       [elements/label ::header-label
                       {:content header-label}]))

(defn header-select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected? @(a/subscribe [:storage.media-picker/no-items-selected?])]
       [elements/button :header-select-button
                        {:disabled? no-items-selected?
                         :preset :select-button :indent :both :keypress {:key-code 13}
                         :on-click [:storage.media-picker/save-selected-items!]}]))

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button picker-id]
                                 :middle-content [header-label         picker-id]
                                 :end-content    [header-select-button picker-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [:<> [header-label-bar picker-id]
       [item-browser/header :storage.media-picker
                            {:new-item-event   [:storage.media-picker/add-new-item!]
                             :new-item-options [:create-directory! :upload-files!]}]])



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-selection-bar-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected?  @(a/subscribe [:storage.media-picker/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-picker/get-selected-item-count])]
       [:<> [elements/label {:content {:content :n-items-selected :replacements [selected-item-count]}
                             :color :muted :min-height :l :font-size :xs}]
            [elements/icon-button {:color :default :height :l :preset :close :disabled? no-items-selected?
                                   :on-click [:storage.media-picker/discard-selection!]}]]))

(defn footer-selection-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/row {:content [footer-selection-bar-structure picker-id]
                 :horizontal-align :right}])

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [footer-selection-bar picker-id])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [id] :as media-item}]
  [elements/toggle {:content  [directory-item media-item {:icon :navigate_next}]
                    :on-click [:item-browser/browse-item! :storage.media-picker id]
                    :hover-color :highlight}])

(defn file-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item]
  (if-let [file-selectable? @(a/subscribe [:storage.media-picker/file-selectable? media-item])]
          (let [file-selected? @(a/subscribe [:storage.media-picker/file-selected? media-item])]
               [elements/toggle {:content  [file-item media-item {:icon (if file-selected? :check_circle_outline :radio_button_unchecked)}]
                                 :on-click [:storage.media-picker/file-clicked media-item]
                                 :hover-color :highlight}])
          [file-item media-item {:disabled? true}]))


(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item-toggle media-item]
                                      [file-item-toggle      media-item]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [item-browser/body :storage.media-picker
                     {:item-path     [:storage :media-picker/browsed-item]
                      :items-path    [:storage :media-picker/downloaded-items]
                      :label-key     :alias
                      :list-element  #'media-item
                      :root-item-id  core.config/ROOT-DIRECTORY-ID
                      :search-keys   [:alias]}])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- n-items-selected-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [multiple?]}]
  (let [no-items-selected?  @(a/subscribe [:storage.media-picker/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-picker/get-selected-item-count])]
       [elements/label {:color :muted :min-height :s
                        :content (cond (and multiple? no-items-selected?) :no-items-selected
                                       no-items-selected?                 :no-item-selected
                                       :default {:content :n-items-selected :replacements [selected-item-count]})}]))

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [disabled? indent label]}]
  (if label [elements/label {:content label :min-height :s :disabled? disabled? :indent indent}]))

(defn media-picker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id {:keys [disabled? indent] :as picker-props}]
  [elements/toggle {:content  [n-items-selected-label picker-id]
                    :on-click [:storage.media-picker/load-picker! picker-id picker-props]
                    :disabled? disabled?
                    :indent    indent}])

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:<> [media-picker-label  picker-id picker-props]
       [media-picker-button picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))
