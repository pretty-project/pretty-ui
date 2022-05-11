
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.views
    (:require [extensions.storage.core.config           :as core.config]
              [extensions.storage.media-browser.helpers :as media-browser.helpers]
              [layouts.surface-a.api                    :as surface-a]
              [mid-fruits.format                        :as format]
              [mid-fruits.keyword                       :as keyword]
              [mid-fruits.io                            :as io]
              [plugins.item-browser.api                 :as item-browser]
              [x.app-components.api                     :as components]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.api                       :as elements]
              [x.app-media.api                          :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn storage-label
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          (let [directory-alias @(a/subscribe [:item-browser/get-current-item-label :storage.media-browser])]
               [:<> [surface-a/title-sensor {:title directory-alias :offset 36}]
                    [elements/label ::storage-label
                                    {:content     directory-alias
                                     :font-size   :xxl
                                     :font-weight :extra-bold
                                     :indent      {:top :xxl}}]])
          [elements/ghost {:height :l :indent {:bottom :xs :top :xxl} :style {:width "180px"}}]))

(defn directory-description
  []
  (let [size  @(a/subscribe [:db/get-item [:storage :media-browser/browsed-item :size]])
        items @(a/subscribe [:db/get-item [:storage :media-browser/browsed-item :items]])
        size   (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
                    (components/content {:content :n-items :replacements [(count items)]}))]
       (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
               [elements/label ::directory-description
                               {:color     :muted
                                :content   (if data-received? size)
                                :font-size :xxs
                                :indent    {:bottom :s}}]
               [elements/ghost {:height :s :indent {:bottom :s} :style {:width "150px"}}])))

(defn search-items-field
  []
  (if-let [first-data-received? @(a/subscribe [:item-browser/first-data-received? :storage.media-browser])]
          (let [search-event [:item-browser/search-items! :storage.media-browser {:search-keys [:alias]}]]
               [elements/search-field ::search-items-field
                                      {:indent        {:top :s}
                                       :on-empty      search-event
                                       :on-type-ended search-event
                                       :placeholder   "Keresés a mappában"}])
          [elements/ghost {:height :l :indent {:top :s}}]))

(defn search-description
  []
  (let [data-received?    @(a/subscribe [:item-browser/data-received?     :storage.media-browser])
        browser-disabled? @(a/subscribe [:item-browser/browser-disabled?  :storage.media-browser])
        all-item-count    @(a/subscribe [:item-browser/get-all-item-count :storage.media-browser])]
       [elements/label ::client-list-label
                       {:color            :muted
                        :content          (if data-received? (str "Találatok ("all-item-count")"))
                        :disabled?        browser-disabled?
                        :font-size        :xxs
                        :horizontal-align :right
                        :indent           {:top :xs}}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item-structure
  [browser-id item-dex {:keys [alias size id items modified-at]}]
  (let [timestamp @(a/subscribe [:activities/get-actual-timestamp modified-at])
        item-count (components/content {:content :n-items :replacements [(count items)]})
        size       (-> size io/B->MB format/decimals (str " MB"))]
       [:div {:style {:align-items "center" :border-bottom "1px solid #f0f0f0" :display "flex"}}
             (let [icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)]
                  [elements/icon {:icon :folder :icon-family icon-family :indent {:horizontal :m :vertical :xl}}])
             [:div {:style {:flex-grow 1}}   [elements/label {:content alias                     :style {:color "#333"}}]]
             [:div {:style {:width "160px"}} [elements/label {:content size       :font-size :xs :style {:color "#888" :line-height "18px"}}]
                                             [elements/label {:content item-count :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             [:div {:style {:width "160px"}} [elements/label {:content timestamp  :font-size :xs :style {:color "#888"}}]]
             [elements/icon {:icon :navigate_next :indent {:right :xs} :size :s}]]))

(defn directory-item
  [browser-id item-dex {:keys [id] :as directory-item}]
  [elements/toggle {:content        [directory-item-structure browser-id item-dex directory-item]
                    :hover-color    :highlight
                    :on-click       [:item-browser/browse-item! :storage.media-browser id]
                    :on-right-click [:storage.media-browser/render-directory-menu! directory-item]}])

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
             [:div {:style {:flex-grow 1}}   [elements/label {:content alias                    :style {:color "#333"}}]]
             [:div {:style {:width "160px"}} [elements/label {:content size      :font-size :xs :style {:color "#888"}}]]
             [:div {:style {:width "160px"}} [elements/label {:content timestamp :font-size :xs :style {:color "#888"}}]]
             [elements/icon {:icon :more_vert :indent {:right :xs} :size :s}]]))

(defn file-item
  [browser-id item-dex file-item]
  [elements/toggle {:content        [file-item-structure browser-id item-dex file-item]
                    :hover-color    :highlight
                    :on-click       [:storage.media-browser/render-file-menu! file-item]
                    :on-right-click [:storage.media-browser/render-file-menu! file-item]}])

(defn media-item
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item browser-id item-dex media-item]
                                      [file-item      browser-id item-dex media-item]))

(defn media-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-browser/body :storage.media-browser
                     {:auto-title?      true
                      :default-item-id   core.config/ROOT-DIRECTORY-ID
                      :default-order-by :modified-at/descending
                      :item-path        [:storage :media-browser/browsed-item]
                      :items-path       [:storage :media-browser/downloaded-items]
                      :items-key        :items
                      :label-key        :alias
                      :list-element     #'media-item
                      :path-key         :path}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-files-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [elements/icon-button ::upload-files-icon-button
                             {:disabled? browser-disabled?
                              :indent    {:right :xxs}
                              :on-click  [:storage.media-browser/upload-files!]
                              :icon      :upload_file}]))

(defn create-folder-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [elements/icon-button ::create-folder-icon-button
                             {:disabled? browser-disabled?
                              :on-click  [:storage.media-browser/create-directory!]
                              :icon      :create_new_folder}]))

(defn go-home-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        at-home?          @(a/subscribe [:item-browser/at-home?          :storage.media-browser])
        error-mode?       @(a/subscribe [:item-browser/get-meta-item     :storage.media-browser :error-mode?])]
       [elements/icon-button ::go-home-icon-button
                             {:disabled? (or browser-disabled? (and at-home? (not error-mode?)))
                              :on-click  [:item-browser/go-home! :storage.media-browser]
                              :preset    :home}]))

(defn go-up-icon-button
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        at-home?          @(a/subscribe [:item-browser/at-home?          :storage.media-browser])]
       [elements/icon-button ::go-up-icon-button
                             {:disabled? (or browser-disabled? at-home?)
                              :indent    {:left :xxs}
                              :on-click  [:item-browser/go-up! :storage.media-browser]
                              :preset    :back}]))


(defn control-bar
  []
  [elements/horizontal-polarity ::control-bar
                                {:start-content [:<> [go-up-icon-button]
                                                     [go-home-icon-button]]
                                 :end-content   [:<> [create-folder-icon-button]
                                                     [upload-files-icon-button]]}])

(defn media-browser-column-label
  [{:keys [label order-by-key]}]
  (let [current-order-by @(a/subscribe [:item-browser/get-current-order-by :storage.media-browser])
        current-order-by-key       (keyword/get-namespace current-order-by)
        current-order-by-direction (keyword/get-name      current-order-by)]
       [elements/button {:color (if (= order-by-key current-order-by-key) :default :muted)
                         :icon  (if (= order-by-key current-order-by-key)
                                    (case current-order-by-direction :descending :arrow_drop_down :ascending :arrow_drop_up))
                         :on-click (if (= order-by-key current-order-by-key)
                                       [:item-browser/swap-items!  :storage.media-browser]
                                       [:item-browser/order-items! :storage.media-browser (keyword/add-namespace order-by-key :descending)])
                         :label            label
                         :font-size        :xs
                         :horizontal-align :left
                         :icon-position    :right
                         :indent           {:horizontal :xxs}}]))

(defn media-browser-header
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          [:div {:style {:background-color "white" :border-bottom "1px solid #ddd" :position "sticky" :top "48px"}}
                [control-bar]
                [:div {:style {:display "flex"}}
                      [:div {:style {:width "108px"}}]
                      [:div {:style {:display "flex" :flex-grow 1}}   [media-browser-column-label {:label :name          :order-by-key :name}]]
                      [:div {:style {:display "flex" :width "160px"}} [media-browser-column-label {:label :size          :order-by-key :size}]]
                      [:div {:style {:display "flex" :width "160px"}} [media-browser-column-label {:label :last-modified :order-by-key :modified-at}]]
                      [:div {:style {:width "36px"}}]]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [storage-label]
       [directory-description]
       [search-items-field]
       [search-description]
       [elements/horizontal-separator {:size :xxl}]
       [:div {:style {:display :flex :flex-direction :column-reverse}}
             [:div {:style {:width "100%"}}
                   [media-browser]]
             [media-browser-header]]
      [elements/horizontal-separator {:size :xxl}]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
