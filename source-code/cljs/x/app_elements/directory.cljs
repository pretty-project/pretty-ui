
; WARNING! DEPRECATED! DO NOT USE!

;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.6.8
; Compatibility: x4.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.directory
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.io             :as io]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  XXX#4671



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:on-click (function)
  ;   :on-context-menu (function)}
  [directory-id {:keys [label on-click on-right-click]}]
  (cond-> (param {:on-mouse-up (engine/blur-element-function directory-id)
                  :title       (components/content {:content label})})
          (some? on-click)
          (assoc :on-click #(a/dispatch on-click))
          (some? on-right-click)
          (assoc :on-context-menu #(do (.preventDefault %)
                                       (a/dispatch on-right-click)))))

(defn- directory-props->on-click-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:multiple-selection? (boolean)(opt)
  ;   :selectable? (boolean)(opt)}
  ;
  ; @return (metamorphic-event)
  [directory-id {:keys [multiple-selection? selectable?]}]
  (cond (and multiple-selection? selectable?)
        [:elements/toggle-stack-option!  directory-id directory-id]
        (boolean selectable?)
        [:elements/toggle-select-option! directory-id directory-id]))

(defn- view-props->directory-selection-subscriber
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:multiple-selection? (boolean)(opt)}
  ;
  ; @return (metamorphic-event)
  [directory-id {:keys [multiple-selection?]}]
  (if multiple-selection? [:elements/option-stacked?  directory-id directory-id]
                          [:elements/option-selected? directory-id directory-id]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;
  ; @return (map)
  ;  {:on-click (metamorphic-event)
  ;   :size (keyword)}
  [directory-id {:keys [selectable?] :as directory-props}]
  (merge {:on-click   (directory-props->on-click-event directory-id directory-props)
          :size       (param :m)}
         (param directory-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (map)
  [db [_ directory-id]]
  ; A {:render-context-surface? ...} tulajdonság értéket a Re-Frame adatbázisból
  ; kapja a directory elem, ezért szükséges feliratkozást használni, akkor is,
  ; ha azt más tulajdonság nem indokolja.
  (r engine/get-element-props db directory-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:item-count (integer)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [item-count]}]
  (if (some? item-count)
      [:div.x-directory--item-count (components/content {:content :n-items :replacements [item-count]})]
      [:div.x-directory--item-count-placeholder]))

(defn- directory-content-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:content-size (B)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [content-size]}]
  (if (some? content-size)
      [:div.x-directory--content-size (io/B->MB content-size)
                                      (str " MB")]))

(defn- directory-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  [:div.x-directory--label
    [:i.x-directory--icon   :folder]
    [:div.x-directory--name [components/content {:content label}]]])

(defn- directory-static-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [directory-id {:keys [label] :as view-props}]
  [:div.x-directory--body
    (directory-body-attributes directory-id view-props)
    [directory-label directory-id view-props]
    [:div.x-directory--details
      [:div.x-directory--item-count-and-content-size
        [directory-item-count   directory-id view-props]
        [directory-content-size directory-id view-props]]]])

(defn- directory-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [directory-id {:keys [label] :as view-props}]
  [:button.x-directory--body
    (directory-body-attributes directory-id view-props)
    [:div.x-directory--details
      [directory-label directory-id view-props]
      [:div.x-directory--item-count-and-content-size
        [directory-item-count   directory-id view-props]
        [directory-content-size directory-id view-props]]]])

(defn- directory-highlighter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:selected? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [directory-id {:keys [selected?] :as view-props}]
  ; Az [elements/file ...] komponens highlighter funkciójának megvalósítása.
  (let [directory-selection-subscriber (view-props->directory-selection-subscriber directory-id view-props)
        directory-selected?            (a/subscribe directory-selection-subscriber)]
       (fn [] [:div.x-directory--highlighter {:data-selected (or (boolean selected?)
                                                                 (deref   directory-selected?))}])))

(defn- directory
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) view-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [directory-id {:keys [on-click] :as view-props}]
  [:div.x-directory
    (engine/selectable-attributes directory-id view-props)
    [:<> [directory-highlighter directory-id view-props]
         (if (some? on-click)
             [directory-button-body directory-id view-props]
             [directory-static-body directory-id view-props])
         [engine/element-stickers directory-id view-props]]])

(defn view
  ; XXX#8711
  ; A directory elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :context-surface tulajdonságként átadott tartalmat.
  ; A directory elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) directory-id
  ; @param (map) directory-props
  ;  {:class (string or vector)(opt)
  ;   :content-size (B)(opt)
  ;   :context-surface (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :height (px)(opt)
  ;   :item-count (integer)(opt)
  ;   :label (metamorphic-content)
  ;   :multiple-selection? (boolean)(opt)
  ;    Default: false
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;   :on-right-click (metamorphic-event)(constant)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;    TODO ...
  ;   :selected? (boolean)(opt)
  ;    Default: false
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;    TODO ...
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
  ;   :width (px)(opt)}
  ;
  ; @usage
  ;  [elements/directory {...}]
  ;
  ; @usage
  ;  [elements/directory :my-directory {...}]
  ;
  ; @usage
  ;  (defn my-context-surface [directory-id directory-props] "Context surface")
  ;  [elements/directory
  ;    :my-directory
  ;    {:context-surface {:content       #'my-context-surface
  ;                       :content-props directory-props}
  ;     :label           "My directory"
  ;     :on-right-click  [:elements/render-context-surface! :my-directory]}]
  ;
  ; @return (component)
  ([directory-props]
   [view (a/id) directory-props])

  ([directory-id directory-props]
   (let [directory-props (a/prot directory-id directory-props directory-props-prototype)]
        [engine/stated-element directory-id
                               {:component     #'directory
                                :element-props directory-props
                                :subscriber    [::get-view-props directory-id]}])))
