
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
              [x.app-media.api                          :as media]
              [x.app-layouts.api                        :as layouts]
              [x.app-ui.api                             :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn storage-label
  []
  (let [directory-alias @(a/subscribe [:item-browser/get-current-item-label :storage.media-browser])]
       (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
               [:<> [surface-a/title-sensor {:title directory-alias :offset 36}]
                    [elements/label ::storage-label
                                    {:content     directory-alias
                                     :font-size   :xxl
                                     :font-weight :extra-bold
                                     :indent      {:top :xxl}}]]
               [elements/ghost {:height :l :indent {:bottom :xs :top :xxl} :style {:width "180px"}}])))

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
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        search-event [:item-lister/search-items! :clients.client-lister {:search-keys [:email-address :name :phone-number]}]]
       [elements/search-field ::search-items-field
                              {:disabled?     browser-disabled?
                               :indent        {:top :s}
                               :on-empty      search-event
                               :on-type-ended search-event
                               :placeholder   "Keresés az ügyfelek között"}]))

(defn search-description
  []
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        all-item-count    @(a/subscribe [:item-lister/get-all-item-count :clients.client-lister])]
       [elements/label ::client-list-label
                       {:color            :muted
                        :content          (str "Találatok ("all-item-count")")
                        :disabled?        browser-disabled?
                        :font-size        :xxs
                        :horizontal-align :right
                        :indent           {:top :xs}}]))



;; -- Item-menu components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-menu-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias] :as media-item}]
  [elements/label ::media-menu-label
                  {:color     :muted
                   :content   alias
                   :font-size :xs
                   :indent    {:horizontal :xxs :left :s}}])

(defn media-menu-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item]
  [elements/horizontal-polarity ::media-menu-header
                                {:start-content [media-menu-label media-item]}])
                                 ;:end-content   [ui/popup-close-icon-button :storage.media-browser/media-menu {}]}])



;; -- Directory-item menu components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-item]
  [:<> [elements/button ::open-directory-button
                        {:hover-color :highlight
                         :icon        :folder
                         :icon-family :material-icons-outlined
                         :indent      {:vertical :xs}
                         :label       :open!
                         :on-click    [:storage.media-browser/open-directory! directory-item]
                         :preset      :default}]
       [elements/button ::copy-directory-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-directory-link! directory-item]
                         :preset      :default}]
       [elements/button ::move-directory-button
                        {:disabled?   true
                         :hover-color :highlight
                         :icon        :drive_file_move
                         :icon-family :material-icons-outlined
                         :indent      {:vertical :xs}
                         :label       :move!
                         :on-click    [:storage.media-browser/move-item! directory-item]
                         :preset      :default}]
       [elements/button ::duplicate-directory-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! directory-item]
                         :preset      :default}]
       [elements/button ::rename-directory-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! directory-item]
                         :preset      :default}]
       [elements/button ::delete-directory-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! directory-item]
                         :preset      :warning}]])



;; -- File-item menu components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [mime-type] :as file-item}]
  [:<> (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:hover-color :highlight
                             :icon :preview
                             :indent {:vertical :xs}
                             :label :file-preview
                             :on-click [:storage.media-browser/preview-file! file-item]
                             :preset :default}])
       [elements/button ::download-file-button
                        {:hover-color :highlight
                         :icon        :cloud_download
                         :indent      {:vertical :xs}
                         :label       :download!
                         :on-click    [:storage.media-browser/download-file! file-item]
                         :preset      :default}]
       [elements/button ::copy-file-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-file-link! file-item]
                         :preset      :default}]
       [elements/button ::move-file-button
                        {:hover-color :highlight
                         :icon        :drive_file_move
                         :indent      {:vertical :xs}
                         :label       :move!
                         :icon-family :material-icons-outlined
                         :on-click    [:storage.media-browser/move-item! file-item]
                         :preset      :default
                         ; TEMP
                         :disabled? true}]
       [elements/button ::duplicate-file-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! file-item]
                         :preset      :default}]
       [elements/button ::rename-file-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! file-item]
                         :preset      :default}]
       [elements/button ::delete-file-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! file-item]
                         :preset      :warning}]])



;; -- Media-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias size id items modified-at] :as media-item}]
  [item-browser/list-item :storage.media-browser item-dex
                          {:icon           :navigate_next
                           :label          (str alias)
                           :description    (media-browser.helpers/directory-item->size   media-item)
                           :header         (media-browser.helpers/directory-item->header media-item)
                           :on-click       [:item-browser/browse-item! :storage.media-browser id]
                           :on-right-click [:storage.media-browser/render-directory-menu! media-item]
                           :timestamp      modified-at}])

(defn file-item_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias filename modified-at] :as media-item}]
  [item-browser/list-item :storage.media-browser item-dex
                          {:icon           :more_vert
                           :label          (str alias)
                           :description    (media-browser.helpers/file-item->size    media-item)
                           :header         (media-browser.helpers/file-item->header  media-item)
                           :on-click       [:storage.media-browser/render-file-menu! media-item]
                           :on-right-click [:storage.media-browser/render-file-menu! media-item]
                           :timestamp      modified-at}])

(defn media-item_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}])
  ;(case mime-type "storage/directory" [directory-item item-dex media-item]
  ;                                    [file-item      item-dex media-item]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn storage-label_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          (let [label @(a/subscribe [:item-browser/get-current-item-label :storage.media-browser])]
               [:<> ;[ui/title-sensor {:title label}]
                    [elements/label ::storage-label
                                    {:content     label
                                     :font-size   :xl
                                     :font-weight :extra-bold
                                     :indent      {:top :xxl}}]])))

(defn storage-directory-content-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          (let [browsed-directory @(a/subscribe [:item-browser/get-current-item :storage.media-browser])
                directory-content  (media-browser.helpers/directory-item->size browsed-directory)]
               [elements/label ::storage-directory-content-label
                               {:color       :muted
                                :content     directory-content
                                :font-size   :xxs
                                :font-weight :extra-bold}])))



;; ----------------------------------------------------------------------------
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
                   [elements/label {:content alias                    :style {:color "#333" :line-height "18px"}}]]
             [:div {:style {:width "140px"}}
                   [elements/label {:content timestamp :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             [:div {:style {:width "140px"}}
                   [elements/label {:content size      :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             (if-let [file-selected? @(a/subscribe [:storage.media-selector/file-selected? file-item])]
                     [elements/icon {:icon :check_circle_outline :indent {:right :xs} :size :s}]
                     [elements/icon {:icon :radio_button_unchecked :indent {:right :xs} :size :s}])]))

(defn file-item
  [browser-id item-dex {:keys [alias modified-at] :as file-item}]
  (let [file-selectable? @(a/subscribe [:storage.media-selector/file-selectable? file-item])]
       [elements/toggle {:content     [file-item-structure browser-id item-dex file-item]
                         :disabled?   (not file-selectable?)
                         :hover-color :highlight
                         :on-click    [:storage.media-selector/file-clicked file-item]}]))

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
          [:div {:style {:background-color "white" :border-bottom "1px solid #ddd" :display "flex" :position "sticky" :top "48px"}}
                [:div {:style {:width "42px"}}]
                [:div {:style {:display "flex" :flex-grow 1}}   [media-browser-column-label {:label :name          :order-by-key :name}]]
                [:div {:style {:display "flex" :width "160px"}} [media-browser-column-label {:label :size          :order-by-key :size}]]
                [:div {:style {:display "flex" :width "160px"}} [media-browser-column-label {:label :last-modified :order-by-key :modified-at}]]
                [:div {:style {:width "36px"}}]]))



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
