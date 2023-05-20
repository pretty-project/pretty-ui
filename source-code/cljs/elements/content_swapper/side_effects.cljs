
(ns elements.content-swapper.side-effects
    (:require [elements.content-swapper.env   :as content-swapper.env]
              [elements.content-swapper.state :as content-swapper.state]
              [re-frame.api                   :as r]
              [time.api                       :as time]
              [vector.api                     :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-fwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content)(opt) page
  ([swapper-id]
   (go-fwd! swapper-id nil))

  ([swapper-id page]
   ; A) If a page passed:
   ;    1. If the page cursor is NOT at the end of page history (the user stepped back at least one page),
   ;       trims the page history vector by removing pages after the cursor.
   ;    2. Adds the new page at the end of the page history.
   ;    3. Steps the page cursor by one. It needs a little break to wait until
   ;       the new page getting mounted into the React-tree.
   ;       50ms seems quite enough (10ms was not).
   ;       Otherwise the new page could be rendered without animation.
   ;    4. Sets the animation direction to forward.
   (when page (if-not (content-swapper.env/on-last-page? swapper-id)
                      (let [page-cursor (get-in @content-swapper.state/SWAPPERS [swapper-id :page-cursor])]
                           (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-history] vector/first-items (inc page-cursor))))
              (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-history] vector/conj-item page)
              (letfn [(f [] (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-cursor] inc))]
                     (time/set-timeout! f 50))
              (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :animation-direction] :fwd))

   ; B) If no page passed:
   ;    If the page cursor is at the end of page history and NO page passed, there is no page to step!
   ;    1. Steps the page cursor by one.
   ;    2. Sets the animation direction to forward.
   (if-not page (when-not (content-swapper.env/on-last-page? swapper-id)
                          (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-cursor] inc)
                          (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] :fwd)))))

(defn go-bwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content)(opt) page
  ([swapper-id]
   (go-bwd! swapper-id nil))

  ([swapper-id page]
   ; A) If a page passed:
   ;    If a page passed to step on backward, that means the page history is changing untrackable!
   ;    Therefore in this case the whole page history is going to be cleared and replaced with the
   ;    given page!
   ;    1. Clears the page history and replaces it with the given page.
   ;    2. Sets the page cursor to 0.
   ;    3. Sets the animation direction to backward.
   (when page (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :page-history] [page])
              (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :page-cursor] 0)
              (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :animation-direction] :bwd))

   ; B) If no page passed:
   ;    If the page cursor is at the beginning of page history and NO page passed, there is no page to step back!
   ;    1. Steps back the page cursor by one.
   ;    2. Sets the animation direction to backward.
   (if-not page (when-not (content-swapper.env/on-first-page? swapper-id)
                          (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-cursor] dec)
                          (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] :bwd)))))

(defn go-home!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  [swapper-id]
  (if-let [initial-page (get-in @content-swapper.state/SWAPPERS [swapper-id :initial-page])]
          (go-bwd! swapper-id initial-page)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) swapper-id
; @param (metamorphic-content)(opt) page
;
; @usage
; [:elements.content-swapper/go-fwd! :my-content-swapper]
;
; @usage
; [:elements.content-swapper/go-fwd! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-fwd! go-fwd!)

; @param (keyword) swapper-id
; @param (metamorphic-content)(opt) page
;
; @usage
; [:elements.content-swapper/go-bwd! :my-content-swapper]
;
; @usage
; [:elements.content-swapper/go-bwd! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-bwd! go-bwd!)

; @param (keyword) swapper-id
;
; @usage
; [:elements.content-swapper/go-home! :my-content-swapper]
(r/reg-fx :elements.content-swapper/go-home! go-home!)
