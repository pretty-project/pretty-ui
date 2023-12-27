
(ns pretty-elements.breadcrumbs.prototypes
    (:require [fruits.noop.api :refer [return]]
              [pretty-elements.element.side-effects :as element.side-effects]
              [dom.api :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [_ {:keys [disabled?]} {:keys [href on-click] :as crumb-props}]
  (cond-> crumb-props
          :inherit-state      (pretty-build-kit/default-values      {:disabled? disabled?})
          :derive-fns/default (pretty-build-kit/default-values      {:content-value-f return :placeholder-value-f return})
          :text/default       (pretty-build-kit/default-values      {:text-color :muted})
          :border/default     (pretty-build-kit/default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default       (pretty-build-kit/default-value-group {:icon :people :icon-color :primary :icon-position :right :icon-size :s})
          (or href on-click)  (pretty-build-kit/default-values      {:text-color :default :click-effect :opacity})
          (or href on-click)  (pretty-build-kit/forced-values       {:on-mouse-up dom/blur-active-element!})
          :on-click/wrap      (pretty-build-kit/value-wrap-fns      {:on-click element.side-effects/dispatch-event-handler!})))





(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:disabled? (boolean)(opt)}
  ; @param (map) crumb-props
  ; {:href (string)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :content-value-f (function)
  ;  :disabled? (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)
  ;  :placeholder-value-f (function)
  ;  :text-color (keyword or string)}
  [_ {:keys [disabled?]} {:keys [href on-click] :as crumb-props}]
  ; @note (#5506)
  ; Each crumb inherits the 'disabled?' property from the breadcrumbs element.
  ; Therefore, the Pretty CSS attribute functions applied on crumbs can apply
  ; the disabled state of the 'breadcrumbs' element on each crumb.
  (merge {:disabled?           disabled?
          :content-value-f     return
          :placeholder-value-f return}
         (if href     {:click-effect :opacity})
         (if on-click {:click-effect :opacity})
         (-> crumb-props)
         (if href       {:on-mouse-up #(dom/blur-active-element!)})
         (if on-click   {:on-click    #(element.side-effects/dispatch-event-handler! on-click)
                         :on-mouse-up #(dom/blur-active-element!)})
         (cond href     {:text-color :default}
               on-click {:text-color :default}
               :static  {:text-color :muted})))

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [_])
