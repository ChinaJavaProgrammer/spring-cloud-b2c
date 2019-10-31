package spring.cloud.user.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

	/**
	 * 冒泡排序
	 * @param intArray
	 * @param k
	 */
	public static int topK(int [] intArray,int k) {
		
		for(int i=0;i<intArray.length;i++) {
			boolean flag = false;
//			System.out.println("第"+(i+1)+"轮排序");
			for(int j=0;j<intArray.length-i-1;j++) {
				if(intArray[j]>intArray[j+1]) {
					int small = intArray[j+1];
					intArray[j+1]=intArray[j];
					intArray[j]=small;
//					for (int v: intArray) {
//						System.out.print(v+"\t");
//					}
//					System.out.println();
					flag=true;
				}
			}
			if(!flag) {
//				System.out.println("排序结束");
				break;
			}
			
		}
		if(k>intArray.length) {
			return intArray[intArray.length-1];
		}else {
			return intArray[intArray.length-1-k];
		}
	}
	/**
	    * 快速排序
	 * @param intArray 
	 * @param index
	 * @param end
	 * @param k
	 */
	public static int fastSort(int [] intArray,int index,int end,int k) {
		if(index<end) {
		//左边开始点位
		int low=index;
		//右边开始点位
		int high=end;
		//基准数据
		int	temp=intArray[index];
		//重复扫描元素步骤
		while(low<high) {
			//从右往左扫描数组
			while(low<high) {
				//如果遍历出的元素比基准数据大则high--
				//如果遍历出的元素比基准数据小则将此元素赋给intArray[low]并且low++
				if(intArray[high]<temp) {
					intArray[low]=intArray[high];
					low++;
					break;
				}else {
					high--;
				}
			}
			//从左往右扫描数组
			while(low<high) {
				if(intArray[low]>=temp) {
					intArray[high]=intArray[low];
					high--;
					break;
				}else {
					low++;
				}
			}
		}	
			//分治 分别对左边和右边部分进行分治处理
			intArray[high]=temp;
			//基数左边部分在进行快速排序
			fastSort(intArray,index,low-1,9);
			//基数右边部分在进行快速排序
			fastSort(intArray,low+1,end,9);
			}
		if(k>intArray.length) {
			return intArray[intArray.length-1];
		}else {
			return intArray[intArray.length-1-k];
		}
}
	
	/**
	    * 归并排序
	 * @param intArray
	 * @param k
	 * @return
	 */
	public static int mergeSort(int [] intArray,int k) {
		int modle=intArray.length/2-1;
		int small=modle/2;
		while(small!=2) {
			small=small/2;
		}
		while(small<=modle) {
			if(intArray[small-2]>intArray[small]) {
				int min=intArray[small];
				intArray[small]=intArray[small-2];
				intArray[small-2]=min;
			}
			
		}
		
		return 0;
}
	public static void main(String[] args) {
		System.out.println(9/2);
		int [] intArray = new int[10000];
		List<Integer> list = new ArrayList<>();
		Random random = new Random();
		for(int i=0;i<intArray.length;i++) {
			int value =random.nextInt(intArray.length)+1;
			if(list.contains(value)) {
				i-=1;
			}else {
				list.add(value);
				intArray[i]=value;
			}
		}
//		int [] intArray = new int[] {6,9,3,4,7,2,8,5};
		System.out.println();
		long start=System.currentTimeMillis();
		System.out.println(start);
//		topK(intArray,9);
		fastSort(intArray,0,intArray.length-1,8);
		long end=System.currentTimeMillis();
		System.out.println(end);
		System.out.println(end-start);
		for (Integer integer : intArray) {
			System.out.print(integer+"\t");
		}
		
	}

}
