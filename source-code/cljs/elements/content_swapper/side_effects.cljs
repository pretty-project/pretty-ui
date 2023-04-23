
(ns elements.content-swapper.side-effects
    (:require [elements.content-swapper.state :as content-swapper.state]
              [re-frame.api                   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  [swapper-id page]
  (case (-> @content-swapper.state/SWAPPERS swapper-id :current-page)
        :a (swap! content-swapper.state/SWAPPERS update swapper-id merge {:current-page :b :page-b page :animation-direction :fwd})
        :b (swap! content-swapper.state/SWAPPERS update swapper-id merge {:current-page :a :page-a page :animation-direction :fwd})))

(defn go-back!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  [swapper-id]
  (case (-> @content-swapper.state/SWAPPERS swapper-id :current-page)
        :a (swap! content-swapper.state/SWAPPERS update swapper-id merge {:current-page :b :animation-direction :bwd})
        :b (swap! content-swapper.state/SWAPPERS update swapper-id merge {:current-page :a :animation-direction :bwd})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:elements.content-swapper/go-to! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-to! go-to!)

; @usage
; [:elements.content-swapper/go-back! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-back! go-back!)
