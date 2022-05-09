
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.views
    (:require [extensions.storage.core.config            :as core.config]
              [extensions.storage.media-browser.helpers  :as media-browser.helpers]
              [extensions.storage.media-selector.helpers :as media-selector.helpers]
              [layouts.popup-a.api                       :as popup-a]
              [mid-fruits.io                             :as io]
              [plugins.item-browser.api                  :as item-browser]
              [x.app-core.api                            :as a]
              [x.app-elements.api                        :as elements]
              [x.app-media.api                           :as media]
              [x.app-ui.api                              :as ui]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  [selector-id])
  ;(let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-selector])]))
        ;on-save       [:storage.media-selector/save-selected-items!]]))
       ;[:<>])) ;[ui/save-popup-header :storage.media-selector/view {:label header-label :on-save on-save}]
            ;[item-browser/header  :storage.media-selector
            ;                      {:new-item-event   [:storage.media-selector/add-new-item!]
            ;                       :new-item-options [:create-directory! :upload-files!]]))



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  [selector-id])
  ;(let [selected-item-count @(a/subscribe [:storage.media-selector/get-selected-item-count])]))
       ;[ui/selection-popup-footer :storage.media-selector/view
        ;                          {:on-discard [:storage.media-selector/discard-selection!]
        ;                           :selected-item-count selected-item-count)]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item-structure
  [browser-id item-dex {:keys [alias id items modified-at]}]
  [:div {:style {:display "flex" :font-weight 500
                 :height "72px" :align-items "center"
                 :border-bottom "1px solid #f0f0f0"}}
        [:div {:style {:width "108px"}}
              [elements/icon {:icon        :folder
                              :icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)}]]
        [:div {:style {:flex-grow 1}}
              [elements/label {:content alias
                               :indent  {:top_ :xs :right :xs}
                               :style   {:color "#333" :line-height "18px"}}]
              [elements/label {:content @(a/subscribe [:activities/get-actual-timestamp modified-at])
                               :font-size :xs
                               :indent    {:bottom_ :xs :right :xs}
                               :style     {:color "#888" :line-height "18px"}}]
              [elements/label {:content   {:content :n-items :replacements [(count items)]}
                               :font-size :xs
                               :indent    {:bottom_ :xs :right :xs}
                               :style     {:color "#888" :line-height "18px"}}]]
        [:div {:style {}}
              [elements/icon {:icon   :navigate_next
                              :indent {:right :xs}
                              :size   :s}]]])

(defn directory-item
  [browser-id item-dex {:keys [id] :as directory-item}]
  [elements/toggle {:content     [directory-item-structure browser-id item-dex directory-item]
                    :hover-color :highlight
                    :on-click    [:item-browser/browse-item! :storage.media-selector id]}])

(defn file-item-structure
  [browser-id item-dex {:keys [alias id modified-at filename]}]
  [:div {:style {:display "flex" :font-weight 500
                 :height "72px" :align-items "center"
                 :border-bottom "1px solid #f0f0f0"}}

        [:div {:style {:width "108px"}}
              (if (io/filename->image? alias)
                  [elements/thumbnail {:border-radius :s
                                       :height        :s
                                       :indent        {:left :xs}
                                       :uri           (media/filename->media-thumbnail-uri filename)
                                       :width         :l}]
                  [elements/icon {:icon :folder}])]
        [:div {:style {:flex-grow 1}}
              [elements/label {:content alias
                               :indent  {:top_ :xs :right :xs}
                               :style   {:color "#333" :line-height "18px"}}]
              [elements/label {:content @(a/subscribe [:activities/get-actual-timestamp modified-at])
                               :font-size :xs
                               :indent    {:bottom_ :xs :right :xs}
                               :style     {:color "#888" :line-height "18px"}}]]
              ;[elements/label {:content   {:content :n-items :replacements [(count items)]}
              ;                 :font-size :xs
              ;                 :indent    {:bottom_ :xs :right :xs}
              ;                 :style     {:color "#888" :line-height "18px"}}]]
        [:div {:style {}}
              [elements/icon {:icon   :navigate_next
                              :indent {:right :xs}
                              :size   :s}]]])

(defn file-item
  [browser-id item-dex {:keys [alias modified-at] :as media-item}]
  (let [file-selectable? @(a/subscribe [:storage.media-selector/file-selectable? media-item])]
       [elements/toggle {:content     [file-item-structure browser-id item-dex directory-item]
                         :disabled?   (not file-selectable?)
                         :hover-color :highlight
                         :on-click    [:storage.media-selector/file-clicked media-item]}]))

(defn media-item
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item browser-id item-dex media-item]
                                      [file-item      browser-id item-dex media-item]))

(defn body
  [selector-id]
  [item-browser/body :storage.media-selector
                     {:default-item-id  core.config/ROOT-DIRECTORY-ID
                      :default-order-by :modified-at/descending
                      :item-path        [:storage :media-selector/browsed-item]
                      :items-path       [:storage :media-selector/downloaded-items]
                      :items-key        :items
                      :label-key        :alias
                      :path-key         :path
                      :list-element     #'media-item}])



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
