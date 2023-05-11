
(ns elements.content-swapper.side-effects
    (:require [elements.content-swapper.state :as content-swapper.state]
              [re-frame.api                   :as r]
              [time.api                       :as time]
              [vector.api                     :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  [swapper-id page]
  ; 1. If the page cursor is not at the end of page history (the user stepped back at least one page),
  ;    trims the page history vector by removing pages after the cursor.
  ; 2. Adds the new page at the end of the page history.
  ; 3. Steps forward the page cursor by one. It needs a little break to wait until
  ;    the new page getting mounted into the React-tree.
  ;    50ms seems quite enough (10ms was not).
  ;    Otherwise the new page could be rendered without animation.
  ; 4. Sets the animation direction to forward.
  (let [page-cursor  (get-in @content-swapper.state/SWAPPERS [swapper-id :page-cursor])
        page-history (get-in @content-swapper.state/SWAPPERS [swapper-id :page-history])]
       (if-let [stepped-back? (not= page-cursor (-> page-history count dec))]
               (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-history] vector/first-items (inc page-cursor))))
  (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-history] conj page)
  (letfn [(f [] (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-cursor] inc))]
         (time/set-timeout! f 50))
  (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] :fwd))

(defn go-back!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  [swapper-id]
  ; 1. Steps back the page cursor by one.
  ; 2. Sets the animation direction to backward.
  (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-cursor] dec)
  (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] :bwd))

(defn go-home!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  [swapper-id]
  (if-let [initial-page (get-in @content-swapper.state/SWAPPERS [swapper-id :initial-page])]
          (go-to! swapper-id initial-page)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:elements.content-swapper/go-to! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-to! go-to!)

; @usage
; [:elements.content-swapper/go-back! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-back! go-back!)

; @usage
; [:elements.content-swapper/go-home! :my-content-swapper]
(r/reg-fx :elements.content-swapper/go-home! go-home!)
