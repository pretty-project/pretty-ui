
(ns pretty-elements.breadcrumbs.prototypes
    (:require [fruits.noop.api :refer [return]]
              [pretty-elements.element.side-effects :as element.side-effects]
              [dom.api :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
