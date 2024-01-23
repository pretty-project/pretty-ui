
(ns pretty-elements.content-swapper.side-effects
    (:require [fruits.map.api                        :refer [dissoc-in]]
              [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [pretty-elements.content-swapper.env   :as content-swapper.env]
              [pretty-elements.content-swapper.state :as content-swapper.state]
              [re-frame.api                          :as r]
              [time.api                              :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (keyword) direction
  ; :fwd, :bwd
  ; @param (metamorphic-content) content
  ; @param (map) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  [swapper-id direction content {:keys [rerender-same?]}]
  ; 1. Adding the new content to the content pool, associated with a randomly generated ID.
  ; 2. Setting the animation direction.
  ; 3. Setting the newly generated content ID as the active content ID (this action mounts the newly added content).
  ;    Without an additional delay (ca. 50ms) the animation does not working properly (10ms was not enough).
  ; 4. Cleaning up the content pool (after the animation ended) if no further content-changing happened
  ;    (the animation has to be disarmed before the cleaning; otherwise, changing the pool can cause an animated rerending of the active content).
  (let [content-id     (random/generate-keyword)
        active-content (content-swapper.env/get-active-content swapper-id)]
       (when (or rerender-same? (not= active-content content))
             (swap! content-swapper.state/SWAPPERS update-in [swapper-id :content-pool] vector/conj-item {:id content-id :content content})
             (swap! content-swapper.state/SWAPPERS assoc-in  [swapper-id :animation-direction] direction)
             (letfn [(f0 [] (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :active-content] content-id))]
                    (time/set-timeout! f0 50))
             (letfn [(f0 [] (let [active-content-id (get-in @content-swapper.state/SWAPPERS [swapper-id :active-content])]
                                 (when (= content-id active-content-id)
                                       (swap! content-swapper.state/SWAPPERS dissoc-in [swapper-id :animation-direction])
                                       (swap! content-swapper.state/SWAPPERS update-in [swapper-id :content-pool] vector/remove-items-by #(not= content-id (:id %))))))]))))
                    ; Cleaning the pool is disabled because it could cause flickering when the user swaps contents at the exact same time
                    ; as the cleaning function works ...
                    ; (time/set-timeout! f0 350)

(defn go-fwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ([swapper-id content]
   (go-fwd! swapper-id content {}))

  ([swapper-id content options]
   (go-to! swapper-id :fwd content options)))

(defn go-bwd!
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ([swapper-id content]
   (go-bwd! swapper-id content {}))

  ([swapper-id content options]
   (go-to! swapper-id :bwd content options)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) swapper-id
; @param (metamorphic-content) content
; @param (map)(opt) options
; {:rerender-same? (boolean)(opt)
;   Default: false}
;
; @usage
; [:pretty-elements.content-swapper/go-fwd! :my-content-swapper [:div "My content"]]
(r/reg-fx :pretty-elements.content-swapper/go-fwd! go-fwd!)

; @param (keyword) swapper-id
; @param (metamorphic-content) content
; @param (map)(opt) options
; {:rerender-same? (boolean)(opt)
;   Default: false}
;
; @usage
; [:pretty-elements.content-swapper/go-bwd! :my-content-swapper [:div "My content"]]
(r/reg-fx :pretty-elements.content-swapper/go-bwd! go-bwd!)
