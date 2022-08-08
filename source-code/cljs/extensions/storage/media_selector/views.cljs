
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.views
    (:require [extensions.storage.core.config :as core.config]
              [layouts.popup-a.api            :as popup-a]
              [mid-fruits.io                  :as io]
              [mid-fruits.format              :as format]
              [plugins.item-browser.api       :as item-browser]
              [x.app-components.api           :as components]
              [x.app-core.api                 :as a]
              [x.app-elements.api             :as elements]
              [x.app-media.api                :as media]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-by-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        order-by-options   [:modified-at/ascending :modified-at/descending :alias/ascending :alias/descending]]
       [elements/icon-button ::order-by-icon-button
                             {:disabled? browser-disabled?
                              :on-click  [:item-browser/choose-order-by! :storage.media-selector
                                                                         {:order-by-options order-by-options}]
                              :preset    :order-by}]))

(defn upload-files-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-selector])]
       [elements/icon-button ::upload-files-icon-button
                             {:disabled? browser-disabled?
                              :on-click  [:storage.media-selector/upload-files!]
                              :icon      :upload_file}]))

(defn create-folder-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-selector])]
       [elements/icon-button ::create-folder-icon-button
                             {:disabled? browser-disabled?
                              :on-click  [:storage.media-selector/create-directory!]
                              :icon      :create_new_folder}]))

(defn go-home-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        at-home?          @(a/subscribe [:item-browser/at-home?          :storage.media-selector])
        error-mode?       @(a/subscribe [:item-browser/get-meta-item     :storage.media-selector :error-mode?])]
       [elements/icon-button ::go-home-icon-button
                             {:disabled? (or browser-disabled? (and at-home? (not error-mode?)))
                              :on-click  [:item-browser/go-home! :storage.media-selector]
                              :preset    :home}]))

(defn go-up-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        at-home?          @(a/subscribe [:item-browser/at-home?          :storage.media-selector])]
       [elements/icon-button ::go-up-icon-button
                             {:disabled? (or browser-disabled? at-home?)
                              :on-click  [:item-browser/go-up! :storage.media-selector]
                              :preset    :back}]))

(defn search-items-field
  []
  (let [search-event [:item-browser/search-items! :storage.media-selector {:search-keys [:alias]}]]
       [elements/search-field ::search-items-field
                              {:autoclear?    true
                               :indent        {:vertical :xxs}
                               :min-width     :s
                               :on-empty      search-event
                               :on-type-ended search-event
                               :placeholder   "Keresés a mappában"}]))

(defn control-bar
  []
  [elements/horizontal-polarity ::control-bar
                                {:start-content [:<> [go-up-icon-button]
                                                     [go-home-icon-button]
                                                     [order-by-icon-button]
                                                     [elements/button-separator {:indent {:vertical :xxs}}]
                                                     [create-folder-icon-button]
                                                     [upload-files-icon-button]]
                                 :end-content   [:<> [search-items-field]]}])

(defn header-label
  []
  (let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-selector])]
       [elements/label ::header-label
                       {:content header-label
                        :indent  {:horizontal :xs}}]))

(defn select-button
  []
  [elements/button ::select-button
                   {:color       :primary
                    :font-size   :xs
                    :hover-color :highlight
                    :indent      {:horizontal :xxs :vertical :xxs}
                    :label       :select
                    :on-click    [:storage.media-selector/save-selected-items!]}])

(defn cancel-button
  []
  [elements/button ::cancel-button
                   {:font-size   :xs
                    :hover-color :highlight
                    :indent      {:horizontal :xxs :vertical :xxs}
                    :label       :cancel!
                    :on-click    [:ui/close-popup! :storage.media-selector/view]}])

(defn label-bar
  []
  [elements/horizontal-polarity ::label-bar
                                {:start-content  [cancel-button]
                                 :middle-content [header-label]
                                 :end-content    [select-button]}])

(defn header
  [selector-id]
  (let [autosaving? @(a/subscribe [:db/get-item [:storage :media-selector/meta-items :autosaving?]])]
       (if-not autosaving? [:<> [label-bar]
                                (if-let [first-data-received? @(a/subscribe [:item-browser/first-data-received? :storage.media-selector])]
                                        [control-bar]
                                        [elements/horizontal-separator {:size :xxl}])])))



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn discard-selection-button
  []
  (let [selected-item-count @(a/subscribe [:storage.media-selector/get-selected-item-count])]
       [elements/button ::discard-selection-button
                        {:disabled?     (< selected-item-count 1)
                         :font-size     :xs
                         :icon          :close
                         :icon-position :right
                         :indent        {:right :xxs}
                         :on-click      [:storage.media-selector/discard-selection!]
                         :label         {:content :n-items-selected :replacements [selected-item-count]}}]))

(defn footer
  [selector-id]
  (let [autosaving? @(a/subscribe [:db/get-item [:storage :media-selector/meta-items :autosaving?]])]
       (if-not autosaving? [elements/row ::footer
                                         {:content          #'discard-selection-button
                                          :horizontal-align :right}])))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item-structure
  [browser-id item-dex {:keys [alias size id items modified-at]}]
  (let [timestamp @(a/subscribe [:activities/get-actual-timestamp modified-at])
        size       (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
                        (components/content {:content :n-items :replacements [(count items)]}))]
       [:div {:style {:align-items "center" :border-bottom "1px solid #f0f0f0" :display "flex"}}
             (let [icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)]
                  [elements/icon {:icon :folder :icon-family icon-family :indent {:horizontal :m :vertical :xl}}])
             [:div {:style {:flex-grow 1}}
                   [elements/label {:content alias                    :style {:color "#333" :line-height "18px"}}]
                   [elements/label {:content timestamp :font-size :xs :style {:color "#888" :line-height "18px"}}]
                   [elements/label {:content size      :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             [elements/icon {:icon :navigate_next :indent {:right :xs} :size :s}]]))

(defn directory-item
  [browser-id item-dex {:keys [id] :as directory-item}]
  [elements/toggle {:content     [directory-item-structure browser-id item-dex directory-item]
                    :hover-color :highlight
                    :on-click    [:item-browser/browse-item! :storage.media-selector id]}])

(defn file-item-structure
  [browser-id item-dex {:keys [alias id modified-at filename size] :as file-item}]
  (let [timestamp @(a/subscribe [:activities/get-actual-timestamp modified-at])
        size       (-> size io/B->MB format/decimals (str " MB"))]
       [:div {:style {:align-items "center" :border-bottom "1px solid #f0f0f0" :display "flex"}}
             (if (io/filename->image? alias)
                 (let [thumbnail-uri (media/filename->media-thumbnail-uri filename)]
                      [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs :vertical :xs}
                                           :uri thumbnail-uri :width :l}])
                 [elements/icon {:icon :insert_drive_file :indent {:horizontal :m :vertical :xl}}])
             [:div {:style {:flex-grow 1}}
                   [elements/label {:content alias                    :style {:color "#333" :line-height "18px"}}]
                   [elements/label {:content timestamp :font-size :xs :style {:color "#888" :line-height "18px"}}]
                   [elements/label {:content size      :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             (if-let [file-selected? @(a/subscribe [:storage.media-selector/file-selected? file-item])]
                     [elements/icon {:icon :check_circle_outline :indent {:right :xs} :size :s}]
                     [elements/icon {:icon :radio_button_unchecked :indent {:right :xs} :size :s}])]))

(defn file-item
  [browser-id item-dex file-item]
  (let [file-selectable? @(a/subscribe [:storage.media-selector/file-selectable? file-item])]
       [elements/toggle {:content     [file-item-structure browser-id item-dex file-item]
                         :disabled?   (not file-selectable?)
                         :hover-color :highlight
                         :on-click    [:storage.media-selector/file-clicked file-item]}]))

(defn media-item
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item browser-id item-dex media-item]
                                      [file-item      browser-id item-dex media-item]))

(defn autosaving-label
  []
  (let [selected-item-count @(a/subscribe [:storage.media-selector/get-selected-item-count])]
       [elements/label ::autosaving-label
                       {:color   :muted
                        :content {:content :n-items-selected :replacements [selected-item-count]}}]))

(defn autosaving-indicator
  []
  [elements/column ::autosaving-indicator
                   {:content             [autosaving-label]
                    :horizontal-align    :center
                    :stretch-orientation :vertical
                    :vertical-align      :center}])

(defn body
  [selector-id]
  (let [autosaving? @(a/subscribe [:db/get-item [:storage :media-selector/meta-items :autosaving?]])]
       (if autosaving? [autosaving-indicator]
                       [item-browser/body :storage.media-selector
                                          {:default-item-id  core.config/ROOT-DIRECTORY-ID
                                           :default-order-by :modified-at/descending
                                           :item-path        [:storage :media-selector/browsed-item]
                                           :items-key        :items
                                           :items-path       [:storage :media-selector/downloaded-items]
                                           :label-key        :alias
                                           :list-element     #'media-item
                                           :path-key         :path}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [selector-id]
  [popup-a/layout :storage.media-selector/view
                  {:body                [body   selector-id]
                   :header              [header selector-id]
                   :footer              [footer selector-id]
                   :min-width           :m
                   :stretch-orientation :vertical}])
