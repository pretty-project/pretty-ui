
(ns components.context-menu.views
    (:require [fruits.random.api   :as random]
              [pretty-elements.api :as pretty-elements]
              [pretty-elements.api :as pretty-elements]
              [pretty-layouts.api  :as pretty-layouts]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-menu-item
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; @param (map) menu-item
  ; {:color (keyword or string)(opt)
  ;   Default: :default
  ;  :label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)}
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  [_ _ {:keys [color label label-placeholder on-click]}]
  [pretty-elements/button {:color             (or color :default)
                           :horizontal-align  :left
                           :hover-color       :highlight
                           :indent            {:vertical :xs}
                           :label             label
                           :label-placeholder label-placeholder
                           :on-click          {:dispatch-n [[:x.ui/remove-popup! :components.context-menu/view] on-click]}}])

(defn- context-menu-body
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [menu-id {:keys [menu-items] :as menu-props}]
  (letfn [(f0 [menu-items menu-item] (conj menu-items [context-menu-item menu-id menu-props menu-item]))]
         [:<> (reduce f0 [:<>] menu-items)
              [pretty-elements/horizontal-separator {:height :xs}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-icon-button
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [_ _]
  [pretty-elements/icon-button ::close-icon-button
                               {:hover-color :highlight
                                :keypress    {:key-code 27}
                                :on-click    [:x.ui/remove-popup! :components.context-menu/view]
                                :preset      :close}])

(defn- header-label
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)}
  [_ {:keys [label label-placeholder]}]
  [pretty-elements/label ::header-label
                         {:color               :muted
                          :content             label
                          :content-placeholder label-placeholder
                          :font-size           :xs
                          :indent              {:horizontal :xs :left :s}}])

(defn- context-menu-header
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props])
  ; use row instead of horizontal-polarity
  ;[pretty-elements/horizontal-polarity ::context-menu-header
  ;                                     {:start-content [header-label      menu-id menu-props]
  ;                                      :end-content   [close-icon-button menu-id menu-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [pretty-layouts/struct-popup :components.context-menu/view
                               {:body   [context-menu-body   menu-id menu-props]
                                :header [context-menu-header menu-id menu-props]
                                :min-width :xs}])

(defn view
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)
  ;  :menu-items (maps in vector)
  ;   [{:color (keyword or string)(opt)
  ;      Default: :default
  ;     :label (metamorphic-content)(opt)
  ;     :label-placeholder (metamorphic-content)(opt)
  ;     :on-click (function or Re-Frame metamorphic-event)(opt)}]}
  ;
  ; @usage
  ; [context-menu {...}]
  ;
  ; @usage
  ; [context-menu :my-context-menu {...}]
  ([menu-props]
   [view (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parameterizing)
   (fn [_ menu-props]
       (let []; menu-props (context-menu.prototypes/menu-props-prototype menu-props)
            [context-menu menu-id menu-props]))))
