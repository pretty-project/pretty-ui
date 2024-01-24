
(ns pretty-elements.breadcrumbs.prototypes
    (:require [dom.api              :as dom]
              [pretty-defaults.api :as pretty-defaults]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [_ {:keys [disabled?]} {:keys [href on-click-f] :as crumb-props}]
  ; ezek nem mind kell!
  (cond-> crumb-props
          :inherit-state       (pretty-defaults/use-default-values      {:disabled? disabled?})
          :derive-fns/default  (pretty-defaults/use-default-values      {})
          :text/default        (pretty-defaults/use-default-values      {:text-color :muted})
          :border/default      (pretty-defaults/use-default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default        (pretty-defaults/use-default-value-group {:icon :people :icon-color :primary :icon-position :right :icon-size :s})
          (or href on-click-f) (pretty-defaults/use-default-values      {:text-color :default :click-effect :opacity})
          (or href on-click-f) (pretty-defaults/force-values       {:on-mouse-up dom/blur-active-element!})))





(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:disabled? (boolean)(opt)}
  ; @param (map) crumb-props
  ; {:href-uri (string)(opt)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :disabled? (boolean)
  ;  :on-click-f (function)
  ;  :on-mouse-up-f (function)
  ;  :text-color (keyword or string)}
  [_ {:keys [disabled?]} {:keys [href-uri on-click-f] :as crumb-props}]
  ; @note (#5506)
  ; Each crumb inherits the 'disabled?' property from the breadcrumbs element.
  ; Therefore, the Pretty CSS attribute functions applied on crumbs can apply
  ; the inherited disabled state on each crumb.
  (merge {:disabled? disabled?}
         (if href-uri   {:click-effect :opacity})
         (if on-click-f {:click-effect :opacity})
         (-> crumb-props)
         (if href-uri   {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f {:on-mouse-up-f dom/blur-active-element!})
         (cond href-uri   {:text-color :default}
               on-click-f {:text-color :default}
               :static    {:text-color :muted})))

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [_])
