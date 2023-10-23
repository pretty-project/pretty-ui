
(ns elements.content-swapper.side-effects
    (:require [elements.content-swapper.env   :as content-swapper.env]
              [elements.content-swapper.state :as content-swapper.state]
              [map.api                        :refer [dissoc-in]]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [time.api                       :as time]
              [vector.api                     :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (keyword) direction
  ; :fwd, :bwd
  ; @param (metamorphic-content) page
  ; @param (map) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  [swapper-id direction page {:keys [rerender-same?]}]
  ; 1. Adding the new page to the page pool, associated with a randomly generated ID.
  ; 2. Setting the animation direction.
  ; 3. Setting the newly generated page ID as the active page ID (this action mounts the newly added page).
  ;    Without an additional delay (50ms) the animation does not working properly (10ms was not enough).
  ; 4. Cleaning up the page pool after the animation ended if no further page changing happened
  ;    (the animation has to be disarmed before the cleaning, otherwise changing the pool can
  ;     cause an animated rerending of the active page).
  (let [page-id     (random/generate-keyword)
        active-page (content-swapper.env/get-active-page swapper-id)]
       (when (or rerender-same? (not= active-page page))
             (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-pool] vector/conj-item {:id page-id :page page})
             (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] direction)
             (letfn [(f [] (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :active-page] page-id))]
                    (time/set-timeout! f 50))
             (letfn [(f [] (let [active-page-id (get-in @content-swapper.state/SWAPPERS [swapper-id :active-page])]
                                (when (= page-id active-page-id)
                                      (swap! content-swapper.state/SWAPPERS dissoc-in [swapper-id :animation-direction])
                                      (swap! content-swapper.state/SWAPPERS update-in [swapper-id :page-pool] vector/remove-items-by #(not= page-id (:id %))))))]))))
                    ; Cleaning the pool is disabled because it could cause flickering when the user swaps pages at the exact same time
                    ; as the cleaning function works ...
                    ; (time/set-timeout! f 350)

(defn go-fwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ([swapper-id page]
   (go-fwd! swapper-id page {}))

  ([swapper-id page options]
   (go-to! swapper-id :fwd page options)))

(defn go-bwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ([swapper-id page]
   (go-bwd! swapper-id page {}))

  ([swapper-id page options]
   (go-to! swapper-id :bwd page options)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) swapper-id
; @param (metamorphic-content) page
; @param (map)(opt) options
; {:rerender-same? (boolean)(opt)
;   Default: false}
;
; @usage
; [:elements.content-swapper/go-fwd! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-fwd! go-fwd!)

; @param (keyword) swapper-id
; @param (metamorphic-content) page
; @param (map)(opt) options
; {:rerender-same? (boolean)(opt)
;   Default: false}
;
; @usage
; [:elements.content-swapper/go-bwd! :my-content-swapper [:div "My page"]]
(r/reg-fx :elements.content-swapper/go-bwd! go-bwd!)
