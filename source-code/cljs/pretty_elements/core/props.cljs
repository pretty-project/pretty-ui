
(ns pretty-elements.core.props
    (:require [dom.api                 :as dom]
              [fruits.map.api          :as map]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-defaults.utils   :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn border-props
  ; @ignore
  ;
  ; @description
  ; Applies the default border properties on the given 'element-props' map in case of any border related value is provided.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (utils/use-default-value-group element-props {:border-color    :default
                                                :border-position :all
                                                :border-width    :xxs}
                                               (-> default-props)))

(defn icon-props
  ; @ignore
  ;
  ; @description
  ; Applies the default icon properties on the given 'element-props' map in case of any icon related value is provided.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [font-size text-color] :as element-props} & [default-props]]
  (utils/use-default-value-group element-props {:icon        nil
                                                :icon-family :material-symbols-outlined
                                                :icon-color  :default
                                                :icon-size   :s}
                                               (-> default-props)))

(defn label-icon-props
  ; @ignore
  ;
  ; @description
  ; Applies the default icon properties on the given 'element-props' map in case of any icon related value is provided.
  ; Inherits the color and size values of the text/font properties (if any).
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [font-size text-color] :as element-props} & [default-props]]
  ; Label icons are icons that are attached to the label of the element.
  (utils/use-default-value-group element-props {:icon          nil
                                                :icon-family   :material-symbols-outlined
                                                :icon-position :left
                                                :icon-color    (or text-color :default)
                                                :icon-size     (or font-size  :s)}
                                               (-> default-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-props
  ; @ignore
  ;
  ; @description
  ; Applies the default badge properties on the given 'element-props' map in case of any badge related value is provided.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (map/update-some :badge-content metamorphic-content/compose)
                    (utils/use-default-value-group {:badge-color    :default
                                                    :badge-content  nil
                                                    :badge-position :br}
                                                   (-> default-props))))

(defn marker-props
  ; @ignore
  ;
  ; @description
  ; Applies the default marker properties on the given 'element-props' map in case of any marker related value is provided.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (map/update-some :marker-content metamorphic-content/compose)
                    (utils/use-default-value-group {:marker-content  nil
                                                    :marker-position :tr}
                                                   (-> default-props))))

(defn progress-props
  ; @ignore
  ;
  ; @description
  ; Applies the default progress properties on the given 'element-props' map in case of any progress related value is provided.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-value-group {:progress           0
                                                    :progress-color     :default
                                                    :progress-direction :ltr
                                                    :progress-duration  250}
                                                   (-> default-props))))

(defn tooltip-props
  ; @ignore
  ;
  ; @description
  ; Applies the default tooltip properties on the given 'element-props' map in case of any tooltip related value is provided.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (map/update-some :tooltip-content metamorphic-content/compose)
                    (utils/use-default-value-group {:tooltip-content  nil
                                                    :tooltip-position :right}
                                                   (-> default-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn font-props
  ; @ignore
  ;
  ; @description
  ; Applies the default font properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {:font-size      :s
                                               :font-weight    :medium ; <- lehet alapbol a :normal jobb lenne és a clickable-be átrakni, hogy :static marad normal és a többi medium vagy mind medium
                                               :line-height    :text-block
                                               :letter-spacing :auto}
                                              (-> default-props))))

(defn text-props
  ; @ignore
  ;
  ; @description
  ; Applies the default text properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {:text-overflow :visible}
                                              (-> default-props))))

         ; text-overflow :ellipsis a button-re, label-re, menu-item-ekre
         ; button-re ne legyen ellipsis -> csináljon magának helyet ha nem fér el
         ; menu-items, breadcrumbs, stb pedig legyen scroll-x!

         ; text-color-t is jo lenne default beállítani :default-ra,
         ; vagy maradjon inherited?
         ; a clickable-props beállítja a gombokon de máson mi állítja be?
         ; vagy elég a gombokon?

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-props
  ; @ignore
  ;
  ; @description
  ; Composes the 'content' or 'placeholder' values (if any) in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [content placeholder] :as element-props} & [default-props]]
  (if (-> default-props (map?))
      (-> default-props (merge element-props) content-props)
      (-> element-props (map/assoc-some :content (metamorphic-content/compose content placeholder)))))

(defn label-props
  ; @ignore
  ;
  ; @description
  ; Composes the 'label' or 'placeholder' values (if any) in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [label placeholder] :as element-props} & [default-props]]
  (if (-> default-props (map?))
      (-> default-props (merge element-props) label-props)
      (-> element-props (map/assoc-some :label (metamorphic-content/compose label placeholder)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-props
  ; @ignore
  ;
  ; @description
  ; Applies the default focus properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (utils/use-default-values element-props {} default-props))

(defn clickable-props
  ; @ignore
  ;
  ; @description
  ; Applies the default clickable element properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [href-uri on-click-f] :as element-props} & [default-props]]
  (cond on-click-f (utils/use-default-values element-props {:text-color :default} default-props)
        href-uri   (utils/use-default-values element-props {:text-color :default} default-props)
        :static    (utils/use-default-values element-props {:text-color :muted}   default-props)))

(defn effect-props
  ; @ignore
  ;
  ; @description
  ; Applies the default effect properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [href-uri on-click-f] :as element-props} & [default-props]]
  (cond on-click-f (utils/use-default-values element-props {:click-effect :opacity} default-props)
        href-uri   (utils/use-default-values element-props {:click-effect :opacity} default-props)
        :static    (utils/use-default-values element-props {}                       default-props)))

(defn mouse-event-props
  ; @ignore
  ;
  ; @description
  ; Applies the default mouse event properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; {}
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [{:keys [href-uri on-click-f] :as element-props} & [default-props]]
  (cond on-click-f (utils/use-default-values element-props {:on-mouse-up-f dom/blur-active-element!} default-props)
        href-uri   (utils/use-default-values element-props {:on-mouse-up-f dom/blur-active-element!} default-props)
        :static    (utils/use-default-values element-props {}                                        default-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props
  ; @ignore
  ;
  ; @description
  ; Applies the default flex layout properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {:horizontal-align :center
                                               :orientation      :vertical
                                               :overflow         :visible
                                               :vertical-align   :top}
                                              (-> default-props))))

(defn row-props
  ; @ignore
  ;
  ; @description
  ; Applies the default flex layout properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {:horizontal-align :center
                                               :orientation      :horizontal
                                               :overflow         :visible
                                               :vertical-align   :center}
                                              (-> default-props))))

(defn flex-props
  ; @ignore
  ;
  ; @description
  ; Applies the default flex layout properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  ; A flex layout element can be '{:orientation :horizontal}' or '{:orientation :vertical}'.
  (-> element-props (utils/use-default-values {:horizontal-align :center
                                               :overflow         :visible
                                               :vertical-align   :center}
                                              (-> default-props))))

(defn grid-props
  ; @ignore
  ;
  ; @description
  ; Applies the default grid layout properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {}
                                              (-> default-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn size-props
  ; @ignore
  ;
  ; @description
  ; Applies the default size properties on the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [element-props & [default-props]]
  (-> element-props (utils/use-default-values {}
                                              (-> default-props))))
