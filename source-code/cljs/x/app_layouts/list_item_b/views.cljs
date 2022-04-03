
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.list-item-b.views
    (:require [mid-fruits.css       :as css]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-item-alias-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias]} _]
  [elements/label {:min-height :xs :content alias}])

(defn media-item-modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [modified-at]} _]
  [elements/label {:content @(a/subscribe [:activities/get-actual-timestamp modified-at])
                   :font-size :xs :min-height :xs :selectable? false :color :muted}])

(defn directory-item-content-size-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [content-size items]} _]
  [:div {:style {:display :flex}}
        ;[elements/label {:content (-> content-size io/B->MB format/decimals (str " MB"))
        ;                 :font-size :xs :min-height :xs :selectable? false :color :muted}}
        [elements/label {:font-size :xs :min-height :xs :selectable? false :color :muted :indent :both :content "|"}]
        [elements/label {:content {:content :n-items :replacements [(count items)] :prefix "" :suffix ""}
                         :font-size :xs :min-height :xs :selectable? false :color :muted}]])

(defn file-item-filesize-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [filesize]} _])
  ;[elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
  ;                 :min-height :xs :selectable? false :color :muted :font-size :xs])

(defn directory-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn directory-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [items]} _])
  ;(let [icon-family (if (vector/nonempty? items) :material-icons-filled :material-icons-outlined)]
  ;     [:div.storage--media-item--header [elements/icon {:icon-family icon-family :icon :folder :size :xxl}]]])

(defn directory-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias content-size items] :as media-item} item-props]
  [:div.storage--media-item--details
    [media-item-alias-label             media-item item-props]
    [media-item-modified-at-label       media-item item-props]
    [directory-item-content-size-labels media-item item-props]])

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item {:keys [disabled?] :as item-props}]
  [:div.storage--media-item {:data-disabled (boolean disabled?)}
                            [directory-item-header  media-item item-props]
                            [directory-item-details media-item item-props]
                            [directory-item-icon    media-item item-props]])

(defn file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [filename]} _])
  ;(let [preview-uri (media/filename->media-thumbnail-uri filename)]
  ;     [:div.storage--media-item--preview {:style {:background-image (css/url preview-uri)}}]])

(defn file-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias] :as media-item} item-props]
  [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]])
                                    ;(if (io/filename->image? alias)
                                    ;    [file-item-preview media-item item-props]]])

(defn file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias filesize] :as media-item} item-props]
  [:div.storage--media-item--details [media-item-alias-label       media-item item-props]
                                     [media-item-modified-at-label media-item item-props]
                                     [file-item-filesize-label     media-item item-props]])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item {:keys [disabled?] :as item-props}]
  [:div.storage--media-item {:data-disabled (boolean disabled?)}
                            [file-item-header  media-item item-props]
                            [file-item-details media-item item-props]
                            [file-item-icon    media-item item-props]])






;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:timestamp (string)(opt)}
  [_ {:keys [timestamp]}]
  [:div.x-list-item-a--secondary-details [:div.x-list-item-a--timestamp (components/content timestamp)]])

(defn- list-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)}
  [_ {:keys [description label] :as item-props}]
  [:div.x-list-item-a--primary-details [:div.x-list-item-a--label       (components/content label)]
                                       [:div.x-list-item-a--description (components/content description)]])

(defn- list-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:thumbnail (map)
  ;    {:icon (keyword)(opt)
  ;     :uri (string)(opt)}}
  [item-dex {:keys [thumbnail] :as item-props}]
  [:div.x-list-item-b--header (if-let [icon (:icon thumbnail)]
                                      [elements/icon {:icon (:icon thumbnail)}])
                              (if-let [uri (:uri thumbnail)]
                                      [:div.x-list-item-b--thumbnail {:style {:background-image (css/url uri)}}])])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-b--details ; [list-item-primary-details   item-dex item-props]
                               ;[list-item-secondary-details item-dex item-props]])
                      (str item-props)])

(defn- list-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-b--icon]) ;[list-item-primary-details   item-dex item-props
                            ;   [list-item-secondary-details item-dex item-props]])

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-b [list-item-header  item-dex item-props]
                      [list-item-details item-dex item-props]
                      [list-item-icon    item-dex item-props]])

(defn toggle-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)}
  [item-dex {:keys [disabled? on-click] :as item-props}]
  [elements/toggle {:content   [list-item-structure item-dex item-props]
                    :disabled? disabled?
                    :on-click  on-click
                    :hover-color :highlight}])

(defn static-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [list-item-structure item-dex item-props])

(defn list-item
  ; @param (integer)(opt) item-dex
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :size (string)(opt)
  ;   :thumbnail (map)
  ;    {:icon (keyword)(opt)
  ;     :uri (string)(opt)}
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [layouts/list-item-b {...}]
  ;
  ; @usage
  ;  [layouts/list-item-b 0 {...}]
  ([item-props]
   [list-item 0 item-props])

  ([item-dex {:keys [on-click] :as item-props}]
   (if on-click [toggle-list-item item-dex item-props]
                [static-list-item item-dex item-props])))
