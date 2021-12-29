
; WARNING! DEPRECATED! DO NOT USE!

;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.6.6
; Compatibility: x4.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.file
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.io             :as io]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  XXX#4671
;  A különösen hosszú fájlnevek megjeleníthetősége miatt, a fájlnév betűmérete,
;  betűtávolsága, a fájlnév előtt megjelenő ikon mérete és a padding-ek mérete
;  csökkentve lettek.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-PREVIEW-ICON :insert_drive_file)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:on-click (function)
  ;   :on-context-menu (function)}
  [file-id {:keys [label on-click on-right-click]}]
  (cond-> (param {:on-mouse-up (engine/blur-element-function file-id)
                  :title       (components/content {:content label})})
          (some? on-click)
          (assoc :on-click #(a/dispatch on-click))
          (some? on-right-click)
          (assoc :on-context-menu #(do (.preventDefault %)
                                       (a/dispatch on-right-click)))))

(defn- view-props->file-preview-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:thumbnail-uri (string)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [{:keys [thumbnail-uri]}]
  (if (some? thumbnail-uri)
      {:style {:background-image    (css/url thumbnail-uri)
               :background-position :center
               :background-repeat   :no-repeat
               :background-size     :cover}}))

(defn- view-props->render-file-preview-icon?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:thumbnail-uri (string)(opt)}
  ;
  ; @return (boolean)
  [{:keys [thumbnail-uri]}]
  (nil? thumbnail-uri))

(defn- file-props->on-click-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:multiple-selection? (boolean)(opt)
  ;   :selectable? (boolean)(opt)}
  ;
  ; @return (metamorphic-event)
  [file-id {:keys [multiple-selection? selectable?]}]
  (cond (and multiple-selection? selectable?)
        [:elements/toggle-stack-option!  file-id file-id]
        (boolean selectable?)
        [:elements/toggle-select-option! file-id file-id]))

(defn- view-props->file-selection-subscriber
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:multiple-selection? (boolean)(opt)}
  ;
  ; @return (metamorphic-event)
  [file-id {:keys [multiple-selection?]}]
  (if multiple-selection? [:elements/option-stacked?  file-id file-id]
                          [:elements/option-selected? file-id file-id]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;
  ; @return (map)
  ;  {:icon (keyword)
  ;   :icon-family (keyword)
  ;   :on-click (metamorphic-event)
  ;   :size (keyword)
  ;   :value-path (item-path vector)}
  [file-id {:keys [selectable?] :as file-props}]
  (merge {:icon        DEFAULT-PREVIEW-ICON
          :icon-family :material-icons-filled
          :on-click    (file-props->on-click-event file-id file-props)
          :size        :m
          :value-path  (engine/default-value-path file-id)}
         (param file-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ;
  ; @return (map)
  [db [_ file-id]]
  ; A {:render-context-surface? ...} tulajdonság értéket a Re-Frame adatbázisból
  ; kapja a directory elem, ezért szükséges feliratkozást használni, akkor is,
  ; ha azt más tulajdonság nem indokolja.
  (r engine/get-element-props db file-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:icon (keyword)}
  ;
  ; @return (hiccup)
  [_ {:keys [icon] :as view-props}]
  [:div.x-file--preview
    (view-props->file-preview-attributes view-props)
    (if (view-props->render-file-preview-icon? view-props)
        [:i.x-file--preview-icon icon])])

(defn- file-timestamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:timestamp (string)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [timestamp]}]
  (if (some? timestamp)
      [:div.x-file--timestamp (str timestamp)]
      [:div.x-file--timestamp-placeholder]))

(defn- file-filesize
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:filesize (B)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [filesize]}]
  (if (some? filesize)
      [:div.x-file--filesize (io/B->MB filesize)
                             (str   " MB")]))

(defn- file-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  [:div.x-file--label
    [:i.x-file--icon   :insert_drive_file]
    [:div.x-file--name [components/content {:content label}]]])

(defn- file-static-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [file-id view-props]
  [:div.x-file--body
    (file-body-attributes file-id view-props)
    [file-preview file-id view-props]
    [:div.x-file--details
      [file-label file-id view-props]
      [:div.x-file--timestamp-and-filesize
        [file-timestamp file-id view-props]
        [file-filesize  file-id view-props]]]])

(defn- file-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [file-id view-props]
  [:button.x-file--body
    (file-body-attributes file-id view-props)
    [file-preview file-id view-props]
    [:div.x-file--details
      [file-label file-id view-props]
      [:div.x-file--timestamp-and-filesize
        [file-timestamp file-id view-props]
        [file-filesize  file-id view-props]]]])

(defn- file-highlighter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:selected? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [file-id {:keys [selected?] :as view-props}]
  ; Az [elements/file ...] komponens újrarenderelődése az előnézeti kép villanásával jár,
  ; ezért a file-selected? tulajdonság nem paraméterként adódik át, hanem a [file-highlighter]
  ; független komponens feliratkozása kezeli.
  (let [file-selection-subscriber (view-props->file-selection-subscriber file-id view-props)
        file-selected?            (a/subscribe file-selection-subscriber)]
       (fn [] [:div.x-file--highlighter {:data-selected (or (boolean selected?)
                                                            (deref   file-selected?))}])))

(defn- file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) view-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [file-id {:keys [on-click] :as view-props}]
  [:div.x-file (engine/selectable-attributes file-id view-props)
               [:<> [file-highlighter file-id view-props]
                    (if (some? on-click)
                        [file-button-body file-id view-props]
                        [file-static-body file-id view-props])
                    [engine/element-stickers file-id view-props]]])

(defn view
  ; XXX#8711
  ; A file elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :context-surface tulajdonságként átadott tartalmat.
  ; A file elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) file-id
  ; @param (map) file-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :context-surface (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription-vector)(opt)}
  ;   :filesize (B)(opt)
  ;   :height (px)(opt)
  ;   :icon (keyword)(opt)
  ;    Default: DEFAULT-PREVIEW-ICON
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :label (metamorphic-content)
  ;   :multiple-selection? (boolean)(opt)
  ;    Default: false
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;    Only w {:selectable? false}
  ;   :on-right-click (metamorphic-event)(constant)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;    TODO ...
  ;   :selected? (boolean)(opt)
  ;    Default: false
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :size (keyword)(opt)
  ;    :xxs, xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :on-click (metamorphic-event)(opt)
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :style (map)(opt)
  ;   :thumbnail-uri (string)(opt)
  ;   :timestamp (string)(opt)
  ;   :value-path (item-path vector)(opt)
  ;   :width (px)(opt)}
  ;
  ; @usage
  ;  [elements/file {...}]
  ;
  ; @usage
  ;  [elements/file :my-file {...}]
  ;
  ; @usage
  ;  (defn my-context-surface [file-id file-props] "Context surface")
  ;  [elements/file
  ;    :my-file
  ;    {:context-surface {:content       #'my-context-surface
  ;                       :content-props file-props}
  ;     :label           "My file"
  ;     :on-right-click  [:elements/render-context-surface! :my-file]}]
  ;
  ; @return (component)
  ([file-props]
   [view (a/id) file-props])

  ([file-id file-props]
   (let [file-props (file-props-prototype file-id file-props)]
        [engine/stated-element file-id
                               {:component     #'file
                                :element-props file-props
                                :subscriber    [::get-view-props file-id]}])))
